package org.kft.sql.common;

import com.google.common.base.CaseFormat;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import org.kft.sql.utils.SqlUtil;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public class ColumnAdapter implements Column {
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
        return null;
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
