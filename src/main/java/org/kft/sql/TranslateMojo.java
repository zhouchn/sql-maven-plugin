package org.kft.sql;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        for (File file : listSqlFiles(directory)) {
            getLog().info("translate " + file.getName());
        }
        recursiveTraversal(directory, file -> getLog().info("translate file " + file.getName()));
    }

    private List<File> listSqlFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(files).filter(f -> f.getName().endsWith(".sql")).collect(Collectors.toList());
    }

    private void recursiveTraversal(File directory, Consumer<File> consumer) {
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".sql")) {
                consumer.accept(file);
            } else if (file.isDirectory()) {
                getLog().info("enter " + file.getName());
                recursiveTraversal(file, consumer);
            }
        }
    }
}
