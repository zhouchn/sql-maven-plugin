package org.kft.sql;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.kft.sql.converter.OpenGaussConverter;
import org.kft.sql.converter.SqlConverter;
import org.kft.sql.utils.FileUtil;

import java.io.File;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author author
 * @since 2024/1/25
 **/
@Mojo(name = "translate", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class TranslateMojo extends AbstractMojo {
    @Parameter(name = "source", required = true)
    private String source;
    @Parameter(name = "target", defaultValue = "src/main/resources")
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
        SqlConverter sqlConverter = new OpenGaussConverter();
        FileUtil.listFiles(directory)
                .stream()
                .filter(FileUtil::isSqlFile)
                .sorted(Comparator.comparing(File::getPath))
                .forEach(file -> handleTargetFile(file, sqlConverter));
    }

    private void handleTargetFile(File file, SqlConverter sqlConverter) {
        getLog().info("translate file " + file.getName());
        try (Stream<String> stream = FileUtil.statements(file, ';')) {
            stream.filter(StringUtils::isNotBlank)
                    .forEach(statement -> {
                        System.out.println("before: \r\n" + statement);
                        String target = sqlConverter.convert(statement);
                        System.out.println("after:  \r\n" + target);
                    });
        }
    }
}
