package org.kft.sql;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.kft.sql.utils.FileUtil;

import java.io.File;

/**
 *
 * @author author
 * @since 2024/1/25
 **/
@Mojo(name = "translate", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class TranslateMojo extends AbstractMojo {
    @Parameter(name = "source", required = true)
    private String source;
    @Parameter(name = "target", required = true)
    private String target;
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("process resources, " + source);
        File directory = new File(project.getBasedir(), source);
        if (!directory.exists() || !directory.isDirectory()) {
            getLog().warn(source + " is not directory");
            return;
        }
        FileUtil.listFiles(directory)
                .stream()
                .filter(FileUtil::isSqlFile)
                .forEach(this::handleTargetFile);
    }

    private void handleTargetFile(File file) {
        getLog().info("translate file " + file.getName());
    }
}
