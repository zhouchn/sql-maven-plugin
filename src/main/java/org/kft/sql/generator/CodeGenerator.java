package org.kft.sql.generator;

import org.kft.sql.common.Table;

/**
 * code generator
 *
 * @author author
 * @since 2024/3/15
 **/
public interface CodeGenerator {
    /**
     * generate source code and write to target file
     *
     * @param table 表定义
     * @param fileType 文件类型
     * @param context 上下文
     */
    void generate(Table table, FileType fileType, GenerateContext context) throws Exception;
}
