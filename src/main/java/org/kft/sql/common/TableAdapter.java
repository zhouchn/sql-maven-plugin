package org.kft.sql.common;

import com.google.common.base.CaseFormat;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.utils.SqlUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public class TableAdapter implements Table {
    private final CreateTable table;

    public TableAdapter(CreateTable table) {
        this.table = table;
    }

    @Override
    public String getTableName() {
        return SqlUtil.removeWrap(table.getTable().getName());
    }

    @Override
    public String getEntityName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getTableName());
    }

    @Override
    public String getEngine() {
        return SqlUtil.nextValue(table.getTableOptionsStrings(), "ENGINE");
    }

    @Override
    public String getComment() {
        String comment = SqlUtil.nextValue(table.getTableOptionsStrings(), "COMMENT");
        return SqlUtil.removeWrap(comment, '\'', "");
    }

    @Override
    public List<Column> getColumns() {
        return table.getColumnDefinitions().stream().map(ColumnAdapter::new).collect(Collectors.toList());
    }

    @Override
    public List<Index> getIndices() {
        return table.getIndexes().stream().map(IndexAdapter::new).collect(Collectors.toList());
    }

    public void addIndex(net.sf.jsqlparser.statement.create.table.Index index) {
        table.getIndexes().add(index);
    }

    public void removeIndex(String indexName) {
        table.getIndexes().removeIf(index -> equalsKey(index.getName(), indexName));
    }

    private boolean equalsKey(String key1, String key2) {
        key1 = SqlUtil.removeWrap(key1);
        key2 = SqlUtil.removeWrap(key2);
        return StringUtils.equals(key1, key2);
    }

    public void addColumn(ColumnDefinition columnDefinition) {
        String after = SqlUtil.nextValue(columnDefinition.getColumnSpecs(), "AFTER");
        if (StringUtils.isBlank(after)) {
            table.getColumnDefinitions().add(columnDefinition);
        } else {
            List<ColumnDefinition> columns = table.getColumnDefinitions();
            int index = 0, len = columns.size();
            while (index < len && !equalsKey(columns.get(index).getColumnName(), after)) {
                index++;
            }
            Preconditions.checkArgument(index < len, "can't find " + after);
            columns.add(index + 1, columnDefinition);
        }
    }

    public void updateColumn(String columnName, ColumnDefinition another) {
        table.getColumnDefinitions().stream().filter(item -> equalsKey(item.getColumnName(), columnName)).forEach(item -> {
            item.setColumnName(MoreObjects.firstNonNull(another.getColumnName(), item.getColumnName()));
            item.setColumnSpecs(MoreObjects.firstNonNull(another.getColumnSpecs(), item.getColumnSpecs()));
            item.setColDataType(MoreObjects.firstNonNull(another.getColDataType(), item.getColDataType()));
        });
    }

    public void removeColumn(String columnName) {
        table.getColumnDefinitions().removeIf(col -> equalsKey(col.getColumnName(), columnName));
    }

    @Override
    public String toString() {
        for (ColumnDefinition definition : table.getColumnDefinitions()) {
            definition.setColumnSpecs(SqlUtil.removeOptions(definition.getColumnSpecs(), "COLLATE"));
        }
        table.setTableOptionsStrings(SqlUtil.removeOptions(table.getTableOptionsStrings(), "COLLATE"));
        return table.toString();
    }
}
