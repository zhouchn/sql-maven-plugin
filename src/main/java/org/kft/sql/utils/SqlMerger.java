package org.kft.sql.utils;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.drop.Drop;
import org.apache.commons.lang3.StringUtils;
import org.kft.sql.common.TableAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/20
 **/
public class SqlMerger {
    private final Appendable output;

    public SqlMerger(Appendable output) {
        this.output = output;
    }

    public void merge(File rootFile) throws IOException {
        Map<String, TableAdapter> tableMap = Maps.newHashMap();
        List<File> files = FileUtil.listFiles(rootFile);
        if (files.isEmpty()) {
            return;
        }
        // merge sql file by file path order
        files.stream().filter(FileUtil::isSqlFile)
                .sorted(Comparator.comparing(File::getPath))
                .forEach(sql -> merge(sql, tableMap));
        for (TableAdapter table : tableMap.values()) {
            output.append(table.toString()).append(";\r\n");
        }
    }

    public void merge(File sqlFile, Map<String, TableAdapter> tableMap) {
        try (Stream<String> stream = FileUtil.statements(sqlFile, ';')) {
            stream.forEach(sql -> {
                try {
                    if (SqlUtil.isDdlStatement(sql)) {
                        merge(sql, tableMap);
                    } else {
                        output.append(sql);
                        output.append(";\r\n");
                    }
                } catch (IOException | JSQLParserException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void merge(String sql, Map<String, TableAdapter> tableMap) throws JSQLParserException, IOException {
        if (StringUtils.startsWithIgnoreCase(sql, "CREATE TABLE")
                && StringUtils.containsIgnoreCase(sql, "UNIQUE INDEX")) {
            // jsqlparser 不支持建表时使用 UNIQUE INDEX
            sql = StringUtils.replaceIgnoreCase(sql, "UNIQUE INDEX", "UNIQUE KEY");
            System.out.println("replace 'UNIQUE INDEX' with 'UNIQUE KEY'");
        }
        try {
            parseAndMerge(sql, tableMap);
        } catch (JSQLParserException e) {
            if ("java.lang.NullPointerException".equals(e.getMessage())) {
                System.out.println("WARNING sql parse failed. remove comment, " + sql);
                // jsqlparser 解析索引时有COMMENT报错
                sql = SqlUtil.removeComment(sql);
                parseAndMerge(sql, tableMap);
            } else {
                throw e;
            }
        }
    }

    private void parseAndMerge(String sql, Map<String, TableAdapter> tableMap) throws JSQLParserException, IOException {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof CreateTable) {
                handleCreateTable((CreateTable) statement, tableMap);
            } else if (statement instanceof Alter) {
                handleAlter((Alter) statement, tableMap);
            } else if (statement instanceof Drop) {
                handleDrop((Drop) statement, tableMap);
            } else if (statement instanceof CreateIndex) {
                handleCreateIndex((CreateIndex) statement, tableMap);
            } else {
                throw new RuntimeException("unsupported sql: " + sql);
            }
        } catch (IllegalArgumentException e) {
            output.append(sql).append(";\r\n");
        }
    }

    private void handleCreateTable(CreateTable createTable, Map<String, TableAdapter> tableMap) {
        TableAdapter table = new TableAdapter(createTable);
        tableMap.put(table.getTableName(), table);
    }

    private void handleCreateIndex(CreateIndex createIndex, Map<String, TableAdapter> tableMap) {
        String tableName = SqlUtil.removeWrap(createIndex.getTable().getName());
        TableAdapter table = requireNotNull(tableMap, tableName);
        String oldType = MoreObjects.firstNonNull(createIndex.getIndex().getType(), "");
        createIndex.getIndex().setType(oldType + (oldType.isEmpty() ? "KEY" : " KEY"));
        createIndex.getIndex().setName(SqlUtil.wrapWith(createIndex.getIndex().getName(), "`"));
        table.addIndex(createIndex.getIndex());
    }

    private void handleAlter(Alter alter, Map<String, TableAdapter> tableMap) {
        String tableName = SqlUtil.removeWrap(alter.getTable().getName());
        TableAdapter table = requireNotNull(tableMap, tableName);
        List<AlterExpression> alterExpressions = alter.getAlterExpressions();
        for (AlterExpression expression : alterExpressions) {
            switch (expression.getOperation()) {
                case ADD:
                    if (expression.hasColumn()) {
                        // add column
                        for (AlterExpression.ColumnDataType columnDataType : expression.getColDataTypeList()) {
                            table.addColumn(columnDataType);
                        }
                    } else if (expression.getIndex() != null) {
                        // add index
                        table.addIndex(expression.getIndex());
                    } else if (expression.getUkName() != null) {
                        // add unique index
                        Index index = new Index();
                        index.setName(expression.getUkName());
                        index.setType("UNIQUE KEY");
                        index.setColumnsNames(expression.getUkColumns());
                        table.addIndex(index);
                    }
                    break;
                case MODIFY:
                    if (expression.hasColumn()) {
                        for (AlterExpression.ColumnDataType columnDataType : expression.getColDataTypeList()) {
                            table.updateColumn(columnDataType.getColumnName(), columnDataType);
                        }
                    }
                    break;
                case DROP:
                    if (expression.getIndex() == null) {
                        // drop column
                        table.removeColumn(expression.getColumnName());
                    } else {
                        // drop index
                        table.removeIndex(expression.getIndex().getName());
                    }
                    break;
                case RENAME:
                    if (expression.hasColumn()) {
                        String newColumn = expression.getColumnName();
                        ColumnDefinition definition = new ColumnDefinition(newColumn, null);
                        table.updateColumn(expression.getColumnOldName(), definition);
                    } else {
                        System.out.println("old index: " + expression.getOldIndex());
                        System.out.println("index: " + expression.getIndex());
                    }
                    break;
                case CHANGE:
                    String oldColumn = SqlUtil.removeWrap(expression.getColumnOldName());
                    table.updateColumn(oldColumn, expression.getColDataTypeList().get(0));
                default:
            }
        }
    }

    private void handleDrop(Drop drop, Map<String, TableAdapter> tableMap) {
        if ("TABLE".equalsIgnoreCase(drop.getType())) {
            // drop table
            String tableName = drop.getName().getName();
            tableMap.remove(SqlUtil.removeWrap(tableName));
        } else if ("INDEX".equalsIgnoreCase(drop.getType())) {
            // drop index
            String tableName = SqlUtil.nextValue(drop.getParameters(), "ON");
            TableAdapter table = requireNotNull(tableMap, tableName);
            table.removeIndex(drop.getName().getName());
        }
    }

    private TableAdapter requireNotNull(Map<String, TableAdapter> tableMap, String tableName) {
        TableAdapter table = tableMap.get(tableName);
        if (table != null) {
            return table;
        }
        throw new IllegalArgumentException(String.format("table %s doesn't exist", tableName));
    }
}
