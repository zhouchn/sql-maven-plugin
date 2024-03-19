package org.kft.sql.generator;

import org.apache.commons.lang3.StringUtils;
import org.kft.sql.common.Table;
import org.kft.sql.utils.FileUtil;

import java.io.File;

/**
 * java source code generator
 *
 * @author author
 * @since 2024/3/15
 **/
public class JavaCodeGenerator implements CodeGenerator {
    @Override
    public void generate(Table table, FileType fileType, GenerateContext context) throws Exception {
        File parent = FileUtil.locateByName(context.sourceCodeDirectory, fileType.getLocation());
        if (parent == null || !parent.exists()) {
            context.log.warn("can't find file " + fileType);
            return;
        }
        String packageName = extractPackageName(parent.getPath());
        String className = table.entityName + fileType.getClassNameSuffix();

        context.template.setSharedVariable("selfPackage", packageName);
        context.template.setSharedVariable("className", className);
        context.template.setSharedVariable("baseClass", context.baseClass);

        File targetFile = new File(parent, className + ".java");
        context.template.render(fileType.getTemplate(), table, targetFile);

        context.template.setSharedVariable(fileType.getValue() + "Package", packageName + "." + className);
    }

    private String extractPackageName(String path) {
        String suffix = StringUtils.substringAfter(path, "src/main/java/");
        suffix = StringUtils.substringBefore(suffix, ".");
        return suffix.replaceAll("/", ".");
    }
}
