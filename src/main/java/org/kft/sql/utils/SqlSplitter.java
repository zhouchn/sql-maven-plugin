package org.kft.sql.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * <description>
 *
 * @author author
 * @since 2024/2/29
 **/
public class SqlSplitter {
    protected static final String[] DML_PREFIX = {"INSERT", "UPDATE", "DELETE", "TRUNCATE"};

    /**
     * 压缩单条SQL语句为1行
     */
    private final boolean compact;

    public SqlSplitter() {
        this(false);
    }

    public SqlSplitter(Boolean compact) {
        this.compact = Boolean.TRUE.equals(compact);
    }

    public void split(File sqlFile, BiConsumer<String, String> resultConsumer) {
        System.out.println("compact:" + compact);
        final StringBuilder ddlBuilder = new StringBuilder();
        final StringBuilder dmlBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(sqlFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String preview = null, line;
            StringBuilder current = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (isBlankOrSingleLineComment(line)) {
                    preview = joinNewLine(preview, line);
                } else if (isCommentLine(line)) {
                    preview = joinNewLine(preview, line);
                } else {
                    current = current == null ? (isStartWithAny(line, DML_PREFIX) ? dmlBuilder : ddlBuilder) : current;
                    append(current, preview, line);
                    current = endWithComma(line) ? null : current;
                    preview = null;
                }
            }
            resultConsumer.accept(ddlBuilder.toString(), dmlBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(String.format("read %s fail", sqlFile.getName()));
        }
    }

    private boolean isBlankOrSingleLineComment(String line) {
        return StringUtils.isBlank(line) || StringUtils.startsWithAny(line, "--", "#");
    }

    private boolean isCommentLine(String line) {
        return StringUtils.startsWith(line, "/*") && StringUtils.countMatches(line, "*/") == 1 && StringUtils.endsWith(line, "*/;");
    }

    private boolean isStartWithAny(String line, CharSequence[] prefix) {
        for (int i = prefix.length - 1; i >= 0; i--) {
            if (StringUtils.startsWithIgnoreCase(line, prefix[i])) {
                return true;
            }
        }
        return false;
    }

    private String joinNewLine(String current, String append) {
        if (current == null) {
            return append;
        }
        return current + "\r\n" + append;
    }

    private void append(StringBuilder builder, String one, String another) {
        if (one != null) {
            builder.append(one).append("\r\n");
        }
        builder.append(another);
        boolean newLine = endWithComma(another) || !compact;
        builder.append(newLine ? "\r\n" : " ");
    }

    /**
     * 判断是否以逗号结尾，跳过空格
     *
     * @param value 字符串
     */
    protected boolean endWithComma(String value) {
        int length = value.length();
        while (length > 0) {
            char v = value.charAt(--length);
            if (!Character.isSpaceChar(v)) {
                return v == ';';
            }
        }
        return false;
    }
}
