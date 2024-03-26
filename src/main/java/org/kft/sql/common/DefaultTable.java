package org.kft.sql.common;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.utils.SqlUtil;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表定义
 *
 * @author author
 * @since 2024/3/15
 **/
public class DefaultTable implements Serializable {
    public String tableName;
    public String entityName;
    public String engine;
    public String charset;
    public String comment;
    public boolean ifNotExists;
    public List<Column> columns;
    public List<Index> indices;

    public static DefaultTable build(CreateTable createTable) {
        DefaultTable table = new DefaultTable();
        table.ifNotExists = createTable.isIfNotExists();
        table.tableName = SqlUtil.removeWrap(createTable.getTable().getName());
        table.engine = SqlUtil.nextValue(createTable.getTableOptionsStrings(), "ENGINE");
        table.charset = SqlUtil.nextValue(createTable.getTableOptionsStrings(), "CHARSET");
        //table.comment = SqlUtil.tableComment(createTable);
        table.columns = createTable.getColumnDefinitions().stream().map(Column::build).collect(Collectors.toList());
        table.indices = createTable.getIndexes().stream().map(Index::build).collect(Collectors.toList());
        return table;
    }

    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEngine() {
        return engine;
    }

    public String getCharset() {
        return charset;
    }

    public String getComment() {
        return comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public List<Index> getIndices() {
        return indices;
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
        public boolean unsigned;
        public String defaultValue;
        public String onUpdate;
        public String javaType;
        public String comment;
        public String fieldName;
        public boolean notNull;
        public boolean autoIncrement;

        public static Column build(ColumnDefinition columnDefinition) {
            Column column = new Column();
            column.name = SqlUtil.removeWrap(columnDefinition.getColumnName());
            column.type = columnDefinition.getColDataType().toString();
            column.unsigned = SqlUtil.contains(columnDefinition.getColumnSpecs(), "UNSIGNED");
            column.defaultValue = SqlUtil.nextValue(columnDefinition.getColumnSpecs(), "DEFAULT");
            column.onUpdate = SqlUtil.nextValue(columnDefinition.getColumnSpecs(), "UPDATE");
            column.notNull = SqlUtil.contains(columnDefinition.getColumnSpecs(), "NOT", "NULL");
            column.autoIncrement = SqlUtil.contains(columnDefinition.getColumnSpecs(), "AUTO_INCREMENT");
            column.comment = SqlUtil.nextValue(columnDefinition.getColumnSpecs(), "COMMENT");
            return column;
        }

        public void update(Column another) {
            this.name = another.name;
            this.type = another.type;
            this.defaultValue = another.defaultValue;
            this.notNull = another.notNull;
            this.autoIncrement = another.autoIncrement;
            this.comment = another.comment;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isUnsigned() {
            return unsigned;
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

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getOnUpdate() {
            return onUpdate;
        }

        public boolean isNotNull() {
            return notNull;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("  `").append(name).append("` ");
            builder.append(type);
            if (unsigned) {
                builder.append(" unsigned");
            }
            if (notNull) {
                builder.append(" NOT NULL");
            }
            if (defaultValue != null) {
                builder.append(" DEFAULT ").append(defaultValue);
            }
            if (onUpdate != null) {
                builder.append(" ON UPDATE ").append(onUpdate);
            }
            if (autoIncrement) {
                builder.append(" AUTO_INCREMENT");
            }
            if (StringUtils.isNotBlank(comment)) {
                builder.append(" COMMENT '").append(comment).append('\'');
            }
            return builder.toString();
        }
    }

    public static class Index {
        public String name;
        public String type;
        public String using; // btree or hash ...
        public List<String> columns;

        public static Index build(net.sf.jsqlparser.statement.create.table.Index index) {
            DefaultTable.Index result = new DefaultTable.Index();
            result.name = SqlUtil.removeWrap(index.getName());
            result.type = SqlUtil.removeWrap(index.getType());
            String using = SqlUtil.nextValue(index.getIndexSpec(), "USING");
            result.using = StringUtils.defaultIfBlank(index.getUsing(), using);
            result.columns = index.getColumnsNames().stream().map(SqlUtil::removeWrap).collect(Collectors.toList());
            return result;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getUsing() {
            return using;
        }

        public List<String> getColumns() {
            return columns;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("  ");
            if (StringUtils.isNotBlank(type)) {
                builder.append(type).append(' ');
            }
            if (StringUtils.isNotBlank(name)) {
                builder.append("KEY `").append(name).append("` ");
            }
            builder.append("(");
            builder.append(columns.stream().map(n -> "`" + n + "`").collect(Collectors.joining(",")));
            builder.append(")");
            if (StringUtils.isNotBlank(using)) {
                builder.append(" USING ").append(using);
            }
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE");
        if (ifNotExists) {
            builder.append(" IF NOT EXISTS");
        }
        builder.append(" `").append(tableName).append("` (\r\n");
        for (Column column : columns) {
            builder.append(column.toString()).append(",\r\n");
        }
        for (int i = 0, len = indices.size(); i < len; i++) {
            builder.append(indices.get(i).toString());
            if (i == len - 1) {
                builder.append("\r\n");
            } else {
                builder.append(",\r\n");
            }
        }
        builder.append(") ");
        if (engine != null) {
            builder.append("ENGINE=").append(engine);
        }
        if (charset != null) {
            builder.append(" DEFAULT CHARSET=").append(charset);
        }
        if (comment != null) {
            builder.append(" COMMENT='").append(comment).append("'");
        }
        return builder.toString();
    }
}
