package org.kft.sql.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/13
 **/
public class FileUtil {
    public static File locateByName(File path, String fileName) {
        File target = new File(path, fileName);
        if (target.exists()) {
            return target;
        }
        return searchFileByName(path, fileName);
    }

    private static File searchFileByName(File root, String fileName) {
        if (root.exists() && root.getName().equals(fileName)) {
            return root;
        }
        File[] files = root.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        for (File file : files) {
            File target = searchFileByName(file, fileName);
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    public static String loadResources(String fileName) throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (resource == null) {
            return null;
        }
        return IOUtils.toString(resource, StandardCharsets.UTF_8);
    }

    public static File getFileFromResource(String fileName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(fileName);
        return resource == null ? null : new File(resource.getFile());
    }

    public static boolean isSqlFile(File file) {
        return file != null && file.isFile() && file.getName().endsWith(".sql");
    }

    public static Stream<String> statements(File file, char delimiter) {
        if (file == null || !file.exists()) {
            return Stream.empty();
        }
        StatementReader statementReader;
        try {
            statementReader = new StatementReader(new FileReader(file), delimiter);
            return statementReader.statements();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 递归遍历指定文件的所有下级文件
     *
     * @param file 扫描的根目录
     * @return 文件列表
     */
    public static List<File> listFiles(File file) {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }
        if (file.isFile()) {
            return Collections.singletonList(file);
        }
        List<File> result = new LinkedList<>();
        recursiveListFiles(file, result);
        return result;
    }

    private static void recursiveListFiles(File file, List<File> result) {
        if (file == null || !file.exists()) {
            return;
        }
        result.add(file);
        if (file.isDirectory() && file.canRead()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File subFile : files) {
                    recursiveListFiles(subFile, result);
                }
            }
        }
    }
}
