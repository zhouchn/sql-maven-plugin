package org.kft.sql.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/29
 **/
public class SqlSplitterTest {

    @Test
    public void split() {
        SqlSplitter sqlSplitter = new SqlSplitter(true);
        File file = FileUtil.getFileFromResource("test1.sql");
        sqlSplitter.split(file, (ddl, dml) -> {
            System.out.println("DDL:\r\n" + ddl);
            System.out.println("DML:\r\n" + dml);
        });
    }

    @Test
    public void testEndWithComma() {
        SqlSplitter sqlSplitter = new SqlSplitter();
        boolean r1 = sqlSplitter.endWithComma("abc");
        assertFalse(r1);
        boolean r2 = sqlSplitter.endWithComma("abc;");
        assertTrue(r2);
        boolean r3 = sqlSplitter.endWithComma("abc; ");
        assertTrue(r3);
    }
}