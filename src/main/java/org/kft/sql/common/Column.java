package org.kft.sql.common;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public interface Column {
    String getName();

    String getType();

    boolean isUnsigned();

    String getJavaType();

    String getComment();

    String getFieldName();
}
