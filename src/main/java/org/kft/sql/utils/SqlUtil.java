package org.kft.sql.utils;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/4
 **/
public class SqlUtil {
    public static String removeWrap(String name) {
        int begin = name.indexOf('`');
        return begin < 0 ? name : name.substring(begin + 1, name.lastIndexOf('`'));
    }

    public static String removeWrap(String name, char c, String defaultValue) {
        if (name == null || name.isEmpty()) {
            return defaultValue;
        }
        int begin = name.indexOf(c);
        return begin < 0 ? name : name.substring(begin + 1, name.lastIndexOf(c));
    }

    public static boolean contains(List<String> specs, String... search) {
        int i = 0;
        for (String spec : specs) {
            if (spec.equalsIgnoreCase(search[i])) {
                i++;
                if (search.length == i) {
                    return true;
                }
            } else if (i != 0) {
                return false;
            }
        }
        return search.length == i;
    }

    public static String nextValue(List<String> specs, String key) {
        for (int i = 0, len = specs.size(); i < len; i++) {
            if (specs.get(i).equalsIgnoreCase(key)) {
                while (++i < len) {
                    if ("=".equals(specs.get(i))) {
                        continue;
                    }
                    return specs.get(i);
                }
            }
        }
        return null;
    }

    public static String tableName(CreateTable createTable) {
        return removeWrap(createTable.getTable().getName());
    }

    public static String tableComment(CreateTable createTable) {
        String comment = nextValue(createTable.getTableOptionsStrings(), "COMMENT");
        return removeWrap(comment, '\'', "");
    }

    public static String columnComment(ColumnDefinition columnDefinition) {
        String comment = nextValue(columnDefinition.getColumnSpecs(), "COMMENT");
        return removeWrap(comment, '\'', "");
    }
}
