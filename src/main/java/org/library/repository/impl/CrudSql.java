package org.library.repository.impl;

import org.library.util.Util;

import java.sql.Connection;

public abstract class CrudSql {

    protected String findById;
    protected String findAll;
    protected String deleteByKey;
    protected Connection connection;

    {
        findById = "select * from %s where id = ?";
        findAll = "select * from %s";
        deleteByKey = "delete from %s where id = ?";
        connection = Util.getConnection();
    }

    protected CrudSql(String tableName) {
        init(tableName);
    }

    protected void setTableName(String tableName) {
        this.init(tableName);
    }

    private void init(String tableName) {
        this.findById = String.format(findById, tableName);
        this.findAll = String.format(findAll, tableName);
        this.deleteByKey = String.format(deleteByKey, tableName);
    }
}
