package org.kft.sql.generator;

import org.apache.commons.lang3.StringUtils;
import org.kft.sql.common.Table;
import org.kft.sql.utils.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Scanner;
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
        File parent = searchByName(context.sourceCodeDirectory, fileType, context.scanner);
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

    private File searchByName(File rootFile, FileType fileType, Scanner scanner) {
        List<File> matched = FileUtil.listFiles(rootFile)
                .stream()
                .filter(f -> f.getName().equals(fileType.getLocation()))
                .collect(Collectors.toList());
        if (matched.isEmpty()) {
            return null;
        }
        if (matched.size() == 1) {
            return matched.get(0);
        }
        printMatchedFiles(matched, fileType);
        return selectParentFile(matched, scanner);
    }

    private void printMatchedFiles(List<File> files, FileType fileType) {
        System.out.println("matched result for " + fileType.getValue());
        for (int i = 0; i < files.size(); i++) {
            System.out.printf("%s: %s\r\n", i, extractPackageName(files.get(i).getPath()));
        }
    }

    private File selectParentFile(List<File> files, Scanner scanner) {
        System.out.print("input number:");
        String input = scanner.nextLine();
        while (!isValidNumber(input, files)) {
            System.out.print("input number:");
            input = scanner.nextLine();
        }
        return files.get(Integer.parseInt(input));
    }

    private boolean isValidNumber(String input, List<File> files) {
        if (!StringUtils.isNumeric(input)) {
            return false;
        }
        int number = Integer.parseInt(input);
        return number >= 0 && number < files.size();
    }

    private String extractPackageName(String path) {
        String suffix = StringUtils.substringAfter(path, "src/main/java/");
        suffix = StringUtils.substringBefore(suffix, ".");
        return suffix.replaceAll("/", ".");
    }
}
