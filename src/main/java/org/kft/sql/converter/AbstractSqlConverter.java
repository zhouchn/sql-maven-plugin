package org.kft.sql.converter;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.drop.Drop;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/23
 **/
public abstract class AbstractSqlConverter implements SqlConverter {

    @Override
    public String convert(String sql) {
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        if (statement instanceof CreateTable) {
            return convertTableCreateSql((CreateTable) statement);
        } else if (statement instanceof Alter) {
            return convertTableAlterSql((Alter) statement);
        } else if (statement instanceof Drop) {
            return convertTableDropSql((Drop) statement);
        } else if (statement instanceof UseStatement) {
            // skip
        } else {
            System.out.println("unsupported sql: " + sql.trim());
        }
        return sql;
    }

    protected abstract String convertTableCreateSql(CreateTable createTable);

    protected abstract String convertTableAlterSql(Alter alter);

    protected abstract String convertTableDropSql(Drop drop);
}
