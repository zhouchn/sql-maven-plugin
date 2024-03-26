package org.kft.sql.common;

import org.kft.sql.utils.SqlUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public class IndexAdapter implements org.kft.sql.common.Index {
    private final net.sf.jsqlparser.statement.create.table.Index index;

    public IndexAdapter(net.sf.jsqlparser.statement.create.table.Index index) {
        this.index = index;
    }

    @Override
    public String getName() {
        return SqlUtil.removeWrap(index.getName());
    }

    @Override
    public String getType() {
        return SqlUtil.removeWrap(index.getType());
    }

    @Override
    public String getUsing() {
        return index.getUsing();
    }

    @Override
    public List<String> getColumns() {
        return index.getColumns().stream().map(col -> SqlUtil.removeWrap(col.columnName)).collect(Collectors.toList());
    }
}
