package org.kft.sql.converter;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.drop.Drop;
import org.apache.commons.lang3.StringUtils;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/23
 **/
public abstract class AbstractSqlConverter implements SqlConverter {
    private static final String CREATE_TABLE_PREFIX = "CREATE TABLE";
    private static final String ALTER_TABLE_PREFIX = "ALTER TABLE";
    private static final String DROP_TABLE_PREFIX = "DROP TABLE";
    private static final String USE = "USE";
    private static final String COMMENTS1 = "--";
    private static final String COMMENTS2 = "/*";

    @Override
    public String convert(String sql) {
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.startsWithIgnoreCase(sql, CREATE_TABLE_PREFIX)) {
            return convertTableCreateSql((CreateTable) statement);
        } else if (StringUtils.startsWithIgnoreCase(sql, ALTER_TABLE_PREFIX)) {
            return convertTableAlterSql((Alter) statement);
        } else if (StringUtils.startsWithIgnoreCase(sql, DROP_TABLE_PREFIX)) {
            return convertTableDropSql((Drop) statement);
        } else if (StringUtils.startsWithIgnoreCase(sql, USE)) {
            // skip
        } else if (StringUtils.startsWithAny(sql, COMMENTS1, COMMENTS2)) {
            // skip
        } else if (StringUtils.isBlank(sql)) {
            // skip
        } else {
            System.out.println("unsupported sql: " + sql);
        }
        return sql;
    }

    protected abstract String convertTableCreateSql(CreateTable createTable);

    protected abstract String convertTableAlterSql(Alter alter);

    protected abstract String convertTableDropSql(Drop drop);
}
