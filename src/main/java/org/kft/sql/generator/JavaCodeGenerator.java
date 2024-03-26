package org.kft.sql.generator;

import org.apache.commons.lang3.StringUtils;
import org.kft.sql.common.Table;
import org.kft.sql.utils.FileUtil;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * java source code generator
 *
 * @author author
 * @since 2024/3/15
 **/
public class JavaCodeGenerator implements CodeGenerator {
    @Override
    public void generate(Table table, FileType fileType, GenerateContext context) throws Exception {
        File parent = searchByName(context.sourceCodeDirectory, fileType.getLocation());
        if (parent == null || !parent.exists()) {
            context.log.warn("can't find file " + fileType);
            return;
        }
        String packageName = extractPackageName(parent.getPath());
        String className = table.getEntityName() + fileType.getClassNameSuffix();

        context.template.setSharedVariable("selfPackage", packageName);
        context.template.setSharedVariable("className", className);
        context.template.setSharedVariable("baseClass", context.baseClass);

        File targetFile = new File(parent, className + ".java");
        context.template.render(fileType.getTemplate(), table, targetFile);

        context.template.setSharedVariable(fileType.getValue() + "Package", packageName + "." + className);
    }

    private File searchByName(File rootFile, String fileName) {
        List<File> files = FileUtil.listFiles(rootFile);
        List<File> matched = files.stream().filter(f -> f.getName().equals(fileName)).collect(Collectors.toList());
        if (matched.isEmpty()) {
            return null;
        }
        if (matched.size() == 1) {
            return matched.get(0);
        }
        System.out.println("match multi file: " + matched.stream().map(File::getName).collect(Collectors.toSet()));
        return matched.get(0);
    }

    private String extractPackageName(String path) {
        String suffix = StringUtils.substringAfter(path, "src/main/java/");
        suffix = StringUtils.substringBefore(suffix, ".");
        return suffix.replaceAll("/", ".");
    }
}
