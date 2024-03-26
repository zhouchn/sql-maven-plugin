package org.kft.sql;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.kft.sql.utils.SqlMerger;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SQL文件合并
 * <p/>
 * 遍历指定目录中的SQL文件，将建表后的DDL合并至主表
 *
 * @author author
 * @since 2024/3/20
 **/
@Mojo(name = "merge", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class MergeMojo extends AbstractMojo {
    @Parameter(name = "source", defaultValue = "src/main/resources")
    private String source;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("process resources, " + source);
        File file = new File(project.getBasedir(), source);
        if (!file.exists() || !file.canRead()) {
            getLog().warn("can't locate file: " + source);
            return;
        }
        File resultFile = generateMergeFile(file);
        getLog().info("merge sql to " + resultFile.getPath());
        try (FileWriter writer = new FileWriter(resultFile)) {
            SqlMerger sqlMerger = new SqlMerger(writer);
            sqlMerger.merge(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File generateMergeFile(File source) {
        String prefix = source.isDirectory() ? source.getName() : StringUtils.removeEnd(source.getName(), ".sql");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss");
        String fileName = String.format("%s_merge_%s.sql", prefix, formatter.format(LocalDateTime.now()));
        return new File(source.getParent(), fileName);
    }

}
