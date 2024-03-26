package org.kft.sql;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.kft.sql.utils.FileUtil;
import org.kft.sql.utils.SqlSplitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * SQL文件拆分
 * <p/>
 * 遍历指定目录中的SQL文件，将原始SQL文件中的DDL和DML语句拆分到独立的文件
 *
 * @author author
 * @since 2024/1/25
 **/
@Mojo(name = "split", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class SplitMojo extends AbstractMojo {
    @Parameter(name = "source", required = true)
    private String source;
    @Parameter(name = "compact")
    private Boolean compact;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private SqlSplitter sqlSplitter;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.sqlSplitter = new SqlSplitter(compact);
        getLog().info("process resources, " + source);
        File directory = new File(project.getBasedir(), source);
        if (!directory.exists() || !directory.isDirectory()) {
            getLog().warn(source + " is not directory");
            return;
        }
        FileUtil.listFiles(directory)
                .stream()
                .filter(FileUtil::isSqlFile)
                .forEach(this::splitSqlFile);
    }

    private void splitSqlFile(File file) {
        sqlSplitter.split(file, (ddl, dml) -> {
            getLog().info("source file: " + file.getAbsolutePath());
            String ddlName = appendNameSuffix(file.getName(), "_ddl");
            String dmlName = appendNameSuffix(file.getName(), "_dml");
            createAndWrite(file.getParent(), ddlName, ddl);
            createAndWrite(file.getParent(), dmlName, dml);
        });
    }

    private String appendNameSuffix(String name, String suffix) {
        int index = StringUtils.lastIndexOf(name, '.');
        if (index < 0) {
            return name + suffix;
        }
        return name.substring(0, index) + suffix + name.substring(index);
    }

    private void createAndWrite(String path, String name, String content) {
        if (StringUtils.isBlank(content)) {
            return;
        }

        String fileName = path + File.separator + name;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            getLog().info("create file: " + fileName);
        } catch (Exception e) {
            getLog().error("generate file failed. fileName: " + fileName);
        }
    }
}
