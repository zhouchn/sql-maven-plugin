package org.kft.sql.common;

import java.util.List;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/22
 **/
public interface Index {
    String getName();
    String getType();
    String getUsing();
    List<String> getColumns();
}
