package org.kft.sql.common;

import java.util.List;

/**
 * 表定义
 *
 * @author author
 * @since 2024/3/15
 **/
public interface Table {
    String getTableName();

    String getEntityName();

    String getEngine();

    String getComment();

    List<Column> getColumns();

    List<Index> getIndices();
}
