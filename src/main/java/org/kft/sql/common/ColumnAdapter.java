package org.kft.sql.common;

import com.google.common.base.CaseFormat;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.utils.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public class ColumnAdapter implements Column {
    /** mysql data type -> java data type */
    private static final Map<String, String> typeMapping = new HashMap<String, String>() {{
        put("char", "String");
        put("varchar", "String");
        put("tinytext", "String");
        put("text", "String");
        put("mediumtext", "String");
        put("longtext", "String");
        put("tinyint", "Integer");
        put("smallint", "Integer");
        put("bit", "Boolean");
        put("int", "Integer");
        put("bigint", "Long");
        put("float", "Float");
        put("double", "Double");
        put("decimal", "BigDecimal");
        put("date", "java.sql.Date");
        put("time", "java.sql.Time");
        put("datetime", "LocalDateTime");
        put("timestamp", "LocalDateTime");
    }};

    private final ColumnDefinition column;

    public ColumnAdapter(ColumnDefinition column) {
        this.column = column;
    }

    @Override
    public String getName() {
        return SqlUtil.removeWrap(column.getColumnName());
    }

    @Override
    public String getType() {
        return column.getColDataType().toString();
    }

    @Override
    public boolean isUnsigned() {
        return SqlUtil.contains(column.getColumnSpecs(), "UNSIGNED");
    }

    @Override
    public String getJavaType() {
        String type = StringUtils.substringBefore(getType(), " ");
        return typeMapping.get(type);
    }

    @Override
    public String getComment() {
        return SqlUtil.nextValue(column.getColumnSpecs(), "COMMENT");
    }

    @Override
    public String getFieldName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, getName());
    }

}
