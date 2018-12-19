package org.mybatis.config;

/**
 * Created by mengtian on 2018-12-19
 */
public enum DBQuerySQL {
    MYSQL("com.mysql.jdbc.Driver",
            "select table_name from information_schema.tables where table_schema = ",
            "table_name");

    private String driverClass;
    private String tableQuerySQL;
    private String fieldTableName;

    DBQuerySQL(String driverClass, String tableQuerySQL, String fieldTableName) {
        this.driverClass = driverClass;
        this.tableQuerySQL = tableQuerySQL;
        this.fieldTableName = fieldTableName;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getTableQuerySQL() {
        return tableQuerySQL;
    }

    public String getFieldTableName() {
        return fieldTableName;
    }}
