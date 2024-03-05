package org.kft.sql.converter;

import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.utils.SqlUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/4
 **/
public abstract class ColumnConverter {
    public String convert(ColumnDefinition definition) {
        ColDataType colDataType = definition.getColDataType();
        if (!isMatch(colDataType.getDataType().toUpperCase())) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String columnName = SqlUtil.removeWrap(definition.getColumnName());
        builder.append("  ").append(columnName);
        builder.append("  ").append(columnType(definition));
        if (isNotEmpty(colDataType.getArgumentsStringList())) {
            builder.append(colDataType.getArgumentsStringList().stream().collect(Collectors.joining(",", "(", ")")));
        }
        List<String> columnSpecs = definition.getColumnSpecs();
        if (SqlUtil.contains(columnSpecs, "NOT", "NULL")) {
            builder.append(" NOT NULL");
        }
        String defaultValue = SqlUtil.nextValue(columnSpecs, "DEFAULT");
        if (StringUtils.isNotBlank(defaultValue)) {
            builder.append(" DEFAULT ").append(defaultValue);
        }
        String another = otherSpecs(columnName, columnSpecs);
        if (StringUtils.isNotBlank(another)) {
            builder.append(' ').append(otherSpecs(columnName, columnSpecs));
        }
        return builder.append(",\r\n").toString();
    }

    abstract boolean isMatch(String columnType);

    protected String columnType(ColumnDefinition definition) {
        return definition.getColDataType().getDataType();
    }

    public <T> boolean isNotEmpty(List<T> list) {
        return list != null && !list.isEmpty();
    }

    protected String otherSpecs(String columnName, List<String> columnSpecs) {
        return "";
    }
}
