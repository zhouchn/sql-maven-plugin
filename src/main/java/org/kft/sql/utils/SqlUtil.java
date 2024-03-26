package org.kft.sql.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/4
 **/
public class SqlUtil {
    public static String removeWrap(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
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

    public static String wrapWith(String value, String extra) {
        if (value == null || value.startsWith(extra)) {
            return value;
        }
        return extra + value + extra;
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
        if (specs == null || specs.size() == 0) {
            return null;
        }
        for (int i = 0, len = specs.size(); i < len; i++) {
            if (specs.get(i).equalsIgnoreCase(key)) {
                while (++i < len) {
                    if ("=".equals(specs.get(i))) {
                        continue;
                    }
                    return removeWrap(specs.get(i), '\'', "");
                }
            }
        }
        return null;
    }

    public static List<String> removeOptions(List<String> options, String key) {
        if (options == null || options.size() == 0) {
            return options;
        }
        int index = 0, len = options.size();
        List<String> result = Lists.newArrayListWithExpectedSize(len);
        while (index < len && !StringUtils.equalsIgnoreCase(options.get(index), key)) {
            result.add(options.get(index++));
        }
        index++;
        if (index >= len) {
            return result;
        }
        index += ("=".equals(options.get(index)) ? 2 : 1);
        while (index < len) {
            result.add(options.get(index++));
        }
        return result;
    }

    public static boolean isDdlStatement(String sql) {
        String[] prefixArray = new String[]{"CREATE", "ALTER", "DROP"};
        for (String prefix : prefixArray) {
            if (StringUtils.startsWithIgnoreCase(sql, prefix)) {
                return true;
            }
        }
        return false;
    }

    public static String removeComment(String sql) {
        StringBuilder builder = new StringBuilder(sql.length());
        int start = 0;
        char firstChar;
        String token = " COMMENT";
        int index = StringUtils.indexOfIgnoreCase(sql, token);
        while (index > -1) {
            builder.append(sql, start, index);
            index += token.length();
            while (index < sql.length() && sql.charAt(index) == ' ') {
                index++;
            }
            firstChar = sql.charAt(index++);
            while (index < sql.length() && (sql.charAt(index) != firstChar)) {
                index++;
            }
            start = ++index;
            index = StringUtils.indexOfIgnoreCase(sql, token, index);
        }
        if (start < sql.length()) {
            builder.append(sql, start, sql.length());
        }
        return builder.toString();
    }
}
