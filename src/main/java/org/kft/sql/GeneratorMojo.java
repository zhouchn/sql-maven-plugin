package org.kft.sql;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * sql table to java code
 *
 * @author author
 * @since 2024/3/13
 **/
@Mojo(name = "code-generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {
    /** mysql data type -> java data type */
    private final Map<String, String> typeMapping = new HashMap<String, String>() {{
        put("char", "String");
        put("varchar", "String");
        put("tinytext", "String");
        put("text", "String");
        put("mediumtext", "String");
        put("longtext", "String");
        put("tinyint", "Integer");
        put("smallint", "Integer");
        put("bit", "Boolean");
        put("int", "Integer");
        put("bigint", "Long");
        put("float", "Float");
        put("double", "Double");
        put("decimal", "BigDecimal");
        put("date", "java.sql.Date");
        put("time", "java.sql.Time");
        put("datetime", "LocalDateTime");
        put("timestamp", "LocalDateTime");
    }};
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

            FileUtil.consumeSegment(sqlFile, ';', sql -> createSourceCode(template, sql));
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
        String input = input(scanner, String.format("Whether to enable %s, yes or no", function));
        return "y".equals(input) || "1".equals(input) || "yes".equals(input);
    }

    private void createSourceCode(FileTemplate template, String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (!(statement instanceof CreateTable)) {
                getLog().warn("skip sql: " + sql);
                return;
            }
            CreateTable createTable = (CreateTable) statement;

            Table table = convertCreateTableToTable(createTable);

            CodeGenerator generator = new JavaCodeGenerator();
            GenerateContext generateContext = new GenerateContext();
            generateContext.log = getLog();
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

    private Table convertCreateTableToTable(CreateTable createTable) {
        Table table = new Table();
        table.tableName = SqlUtil.tableName(createTable);
        table.entityName = LOWER_UNDERSCORE.to(UPPER_CAMEL, table.tableName);
        table.comment = SqlUtil.tableComment(createTable);
        table.columns = createTable.getColumnDefinitions().stream().map(col -> {
            Table.Column column = new Table.Column();
            column.name = SqlUtil.removeWrap(col.getColumnName());
            column.comment = SqlUtil.columnComment(col);
            column.fieldName = LOWER_UNDERSCORE.to(LOWER_CAMEL, column.name);
            column.type = col.getColDataType().getDataType();
            column.javaType = typeMapping.get(col.getColDataType().getDataType().toLowerCase());
            return column;
        }).collect(Collectors.toList());
        return table;
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
