package org.kft.sql;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.kft.sql.common.TableAdapter;
import org.kft.sql.generator.CodeGenerator;
import org.kft.sql.generator.FileTemplate;
import org.kft.sql.generator.FileType;
import org.kft.sql.generator.GenerateContext;
import org.kft.sql.generator.JavaCodeGenerator;
import org.kft.sql.common.Table;
import org.kft.sql.utils.FileUtil;
import org.kft.sql.utils.SqlUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * sql table to java code
 *
 * @author author
 * @since 2024/3/13
 **/
@Mojo(name = "code-generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo {
    /** to be generated fileType list */
    private final List<FileType> fileTypeList = Lists.newArrayList(FileType.ENTITY, FileType.DTO, FileType.QUERY, FileType.MAPPER, FileType.SERVICE, FileType.SERVICE_IMPL, FileType.API_SERVICE, FileType.CONTROLLER);

    @Parameter(defaultValue = "deop.toolkit.mybatis.base.BasicEntity", readonly = true)
    protected String baseEntityClass;

    @Parameter(defaultValue = "deop.toolkit.common.dto.BaseDTO", readonly = true)
    protected String baseDtoClass;

    @Parameter(defaultValue = "deop.toolkit.common.base.Query", readonly = true)
    protected String baseQueryClass;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try (Scanner scanner = new Scanner(System.in)) {
            String sourceFile = input(scanner, "input sql file: ");
            File parent = new File(project.getBasedir(), "src/main/resources");
            File sqlFile = FileUtil.locateByName(parent, sourceFile);
            if (sqlFile == null || !sqlFile.exists() || !sqlFile.isFile()) {
                getLog().error("can't locate file: " + sourceFile);
                return;
            }
            getLog().info("locate file: " + sqlFile.getPath());

            FileTemplate template = FileTemplate.newInstance();
            initCustomVariables(template, scanner);

            try (Stream<String> stream = FileUtil.statements(sqlFile, ';')) {
                stream.filter(StringUtils::isNotBlank)
                        .filter(SqlUtil::isDdlStatement)
                        .forEach(sql -> createSourceCode(template, sql, scanner));
            }
        } catch (Exception e) {
            getLog().error("generate failed.", e);
        }
    }

    private String input(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String sourceFile = scanner.nextLine();
        return sourceFile.trim();
    }

    private void initCustomVariables(FileTemplate template, Scanner scanner) {
        if (enableCertainFunction(scanner, "author")) {
            String authorName = input(scanner, "please input author:");
            template.setSharedVariable("author", authorName);
        }
        template.setSharedVariable("time", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        boolean lombok = enableCertainFunction(scanner, "lombok");
        template.setSharedVariable("enableLombok", lombok);
        boolean swagger = enableCertainFunction(scanner, "swagger");
        template.setSharedVariable("enableSwagger", swagger);
    }

    private boolean enableCertainFunction(Scanner scanner, String function) {
        String input = input(scanner, String.format("Whether to enable %s, yes or no:", function));
        return "y".equals(input) || "1".equals(input) || "yes".equals(input);
    }

    private void createSourceCode(FileTemplate template, String sql, Scanner scanner) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (!(statement instanceof CreateTable)) {
                getLog().warn("skip sql: " + sql);
                return;
            }
            Table table = new TableAdapter((CreateTable) statement);

            CodeGenerator generator = new JavaCodeGenerator();
            GenerateContext generateContext = new GenerateContext();
            generateContext.log = getLog();
            generateContext.scanner = scanner;
            generateContext.template = template;
            generateContext.sourceCodeDirectory = new File(project.getBasedir(), "src/main/java");

            for (FileType fileType : fileTypeList) {
                generateContext.baseClass = getBaseClass(fileType);
                generator.generate(table, fileType, generateContext);
                getLog().info(String.format("create %s file success", fileType));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getBaseClass(FileType fileType) {
        switch (fileType) {
            case DTO:
                return baseDtoClass;
            case QUERY:
                return baseQueryClass;
            case ENTITY:
                return baseEntityClass;
            default:
                return null;
        }
    }
}
