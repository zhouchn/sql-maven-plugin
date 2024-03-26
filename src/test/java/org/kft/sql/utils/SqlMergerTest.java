package org.kft.sql.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/21
 **/
public class SqlMergerTest {
    @Test
    public void testMerge() throws IOException {
        File file = FileUtil.getFileFromResource("merge_test.sql");
        SqlMerger sqlMerger = new SqlMerger(System.out);
        sqlMerger.merge(file);
    }
}