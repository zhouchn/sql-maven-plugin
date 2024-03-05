package org.kft.sql;

/**
 * <description>
 *
 * @author author
 * @since 2024/1/25
 **/
public enum DbType {
    /** mysql */
    MYSQL("mysql"),
    POSTGRESQL("postgresql"),
    OCEAN_BASE("oceanBase"),
    OPEN_GAUSS("openGauss"),
    UNKNOWN("unknown"),
    ;

    private final String value;

    DbType(String value) {
        this.value = value;
    }

    public static DbType of(String value) {
        for (DbType dbType : DbType.values()) {
            if (dbType.value.equalsIgnoreCase(value)) {
                return dbType;
            }
        }
        return DbType.UNKNOWN;
    }
}
