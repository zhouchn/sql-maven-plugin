package org.kft.sql.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

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

    public static String loadResources1(String fileName) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            return null;
        }
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public static void consumeSegment(File file, char delimiter, Consumer<String> consumer) {
        StringBuilder builder = new StringBuilder();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                if (line.indexOf(delimiter) < 0) {
                    builder.append(line);
                } else {
                    int index = line.indexOf(delimiter);
                    builder.append(line, 0, index);
                    consumer.accept(builder.toString());
                    builder.setLength(0);
                    builder.append(line, index + 1, line.length());
                }
            }
            if (builder.length() != 0) {
                consumer.accept(builder.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
