package org.kft.sql.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/20
 **/
public class FileUtilTest {

    @Test
    public void test() {
        File file = FileUtil.getFileFromResource("test2.sql");
        boolean sqlFile = FileUtil.isSqlFile(file);
        assertTrue(sqlFile);
    }

    @Test
    public void statements() {
        File file = FileUtil.getFileFromResource("test2.sql");
        try (Stream<String> stream = FileUtil.statements(file, ';')) {
            stream.forEach(System.out::println);
        }
    }

    @Test
    public void testListFiles() throws IOException {
        File file = FileUtil.getFileFromResource("test.sql");
        List<File> files = FileUtil.listFiles(file);
        assertEquals(1, files.size());

        file = FileUtil.getFileFromResource("templates");
        files = FileUtil.listFiles(file);
        assertEquals(9, files.size());
    }
}
