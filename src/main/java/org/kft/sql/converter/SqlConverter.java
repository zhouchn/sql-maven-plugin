package org.kft.sql.converter;

/**
 * sql converter
 *
 * @author author
 * @since 2024/1/25
 **/
public interface SqlConverter {
    /**
     * 将MySQL语法的sql语句转换到指定数据库的sql语句
     *
     * @param sql 原始sql语句
     * @return 转换后的sql
     */
    String convert(String sql);
}
