package org.kft.sql.common;

import java.util.List;
import java.util.StringJoiner;

/**
 * 表定义
 *
 * @author author
 * @since 2024/3/15
 **/
public class Table {
    public String tableName;
    public String entityName;
    public String comment;
    public List<Column> columns;

    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getComment() {
        return comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    /**
     * 列定义
     *
     * @author author
     * @since 2024/3/14
     **/
    public static class Column {
        public String name;
        public String type;
        public String javaType;
        public String comment;
        public String fieldName;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getJavaType() {
            return javaType;
        }

        public String getComment() {
            return comment;
        }

        public String getFieldName() {
            return fieldName;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Column.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("type='" + type + "'")
                    .add("javaType='" + javaType + "'")
                    .add("comment='" + comment + "'")
                    .add("fieldName='" + fieldName + "'")
                    .toString();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Table.class.getSimpleName() + "[", "]")
                .add("tableName='" + tableName + "'")
                .add("entityName='" + entityName + "'")
                .add("comment='" + comment + "'")
                .add("columns=" + columns)
                .toString();
    }
}
