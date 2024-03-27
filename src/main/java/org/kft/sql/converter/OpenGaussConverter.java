package org.kft.sql.converter;

import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.drop.Drop;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.utils.SqlUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <description>
 *
 * @author author
 * @since 2024/1/25
 **/
public class OpenGaussConverter extends AbstractSqlConverter {
    private static final String PK = "PRIMARY KEY";
    private static final List<ColumnConverter> COLUMN_CONVERTERS;

    static {
        COLUMN_CONVERTERS = new LinkedList<>();
        COLUMN_CONVERTERS.add(new NumberConverter());
        COLUMN_CONVERTERS.add(new StringConverter());
        COLUMN_CONVERTERS.add(new DateConverter());
    }

    @Override
    protected String convertTableCreateSql(CreateTable createTable) {
        String tableName = SqlUtil.removeWrap(createTable.getTable().getName());
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("CREATE TABLE %s%s {\r\n", createTable.isIfNotExists() ? "IF NOT EXISTS " : "", tableName));

        for (ColumnDefinition column : createTable.getColumnDefinitions()) {
            builder.append(convertColumn(column));
        }
        createTable.getIndexes().stream().filter(i -> PK.equals(i.getType())).findAny().ifPresent(pk -> {
            String columns = joiningColumnNames(pk.getColumns());
            builder.append(String.format("  PRIMARY KEY (%s)", columns));
        });
        builder.append("\r\n);\r\n");

        for (Index index : createTable.getIndexes()) {
            builder.append(convertIndex(index, tableName));
        }

        String tableComment = SqlUtil.nextValue(createTable.getTableOptionsStrings(), "COMMENT");
        if (StringUtils.isNotBlank(tableComment)) {
            builder.append(String.format("COMMENT ON TABLE %s IS %s;\r\n", tableName, tableComment));
        }
        for (ColumnDefinition column : createTable.getColumnDefinitions()) {
            builder.append(convertComment(column, tableName));
        }
        return builder.toString();
    }

    private String convertColumn(ColumnDefinition columnDefinition) {
        for (ColumnConverter columnConverter : COLUMN_CONVERTERS) {
            String result = columnConverter.convert(columnDefinition);
            if (StringUtils.isNotBlank(result)) {
                return result;
            }
        }
        return "";
    }

    private String convertIndex(Index index, String tableName) {
        if (PK.equalsIgnoreCase(index.getType())) {
            return "";
        }
        String indexName = SqlUtil.removeWrap(index.getName());
        String columns = joiningColumnNames(index.getColumns());
        String indexType = "UNIQUE KEY".equals(index.getType()) ? "UNIQUE INDEX" : "INDEX";
        return String.format("CREATE %s %s ON %s(%s);\r\n", indexType, indexName, tableName, columns);
    }

    private String joiningColumnNames(List<Index.ColumnParams> columnParams) {
        return columnParams.stream()
                .map(Index.ColumnParams::getColumnName)
                .map(SqlUtil::removeWrap)
                .collect(Collectors.joining(", "));
    }

    private String convertComment(ColumnDefinition columnDefinition, String tableName) {
        String comment = SqlUtil.nextValue(columnDefinition.getColumnSpecs(), "COMMENT");
        if (comment == null || comment.isEmpty()) {
            return "";
        }
        String columnName = SqlUtil.removeWrap(columnDefinition.getColumnName());
        return String.format("COMMENT ON COLUMN %s.%s IS %s;\r\n", tableName, columnName, comment);
    }

    @Override
    protected String convertTableAlterSql(Alter alter) {
        return null;
    }

    @Override
    protected String convertTableDropSql(Drop drop) {
        return null;
    }


    public static class NumberConverter extends ColumnConverter {
        @Override
        boolean isMatch(String columnType) {
            return StringUtils.contains(columnType, "INT");
        }

        @Override
        protected String columnType(ColumnDefinition definition) {
            List<String> columnSpecs = definition.getColumnSpecs();
            String dataType = definition.getColDataType().getDataType();
            if (SqlUtil.contains(columnSpecs, "AUTO_INCREMENT")) {
                return "BIGINT".equalsIgnoreCase(dataType) ? "BIGSERIAL" : "SERIAL";
            } else {
                return "MEDIUMINT".equalsIgnoreCase(dataType) ? "INT" : dataType;
            }
        }

        @Override
        protected String otherSpecs(String columnName, List<String> columnSpecs) {
            if (SqlUtil.contains(columnSpecs, "unsigned")) {
                return String.format(" check(%s >= 0)", columnName);
            }
            return super.otherSpecs(columnName, columnSpecs);
        }
    }

    public static class StringConverter extends ColumnConverter {
        @Override
        boolean isMatch(String columnType) {
            return StringUtils.contains(columnType, "CHAR");
        }

    }

    public static class DateConverter extends ColumnConverter {
        @Override
        boolean isMatch(String columnType) {
            return StringUtils.contains(columnType, "TIME");
        }

        @Override
        protected String columnType(ColumnDefinition definition) {
            return "TIMESTAMP";
        }
    }
}
