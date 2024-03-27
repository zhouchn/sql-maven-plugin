package org.kft.sql.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/20
 **/
public class StatementReader extends BufferedReader {
    private final char delimiter;
    private final StringBuilder buffer;

    public StatementReader(Reader in, char delimiter) {
        super(in, 1024);
        this.delimiter = delimiter;
        this.buffer = new StringBuilder();
    }

    private String readStatement() throws IOException {
        String line, result;
        int index = index(buffer, delimiter);
        if (index > -1) {
            result = buffer.substring(0, index);
            buffer.delete(0, index + 1);
            return result;
        }
        while ((line = readLine()) != null) {
            index = line.indexOf(delimiter);
            if (index < 0) {
                buffer.append(line).append("\r\n");
            } else {
                buffer.append(line, 0, index);
                result = buffer.toString();
                buffer.setLength(0);
                if ((index + 1) < line.length()) {
                    buffer.append(line, index + 1, line.length());
                }
                return result;
            }
        }
        if (buffer.length() == 0) {
            // 文件读取完毕，返回null标识无数据了
            return null;
        } else {
            result = buffer.toString();
            buffer.setLength(0);
            return result;
        }
    }

    private int index(StringBuilder builder, char ch) {
        if (builder == null || builder.length() == 0) {
            return -1;
        }
        return builder.indexOf(String.valueOf(ch));
    }

    public Stream<String> statements() {
        Iterator<String> iterator = new Iterator<String>() {
            String nextSql = null;

            @Override
            public boolean hasNext() {
                if (nextSql != null) {
                    return true;
                } else {
                    try {
                        nextSql = readStatement();
                        return (nextSql != null);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public String next() {
                if (nextSql != null || hasNext()) {
                    String line = nextSql;
                    nextSql = null;
                    return line;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED | Spliterator.NONNULL), false).onClose(this::close);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
