package org.library.repository.impl;

import org.library.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CrudSql {

    protected String findById;
    protected String findAll;
    protected String deleteByKey;
    protected Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(BookDaoImpl.class);

    protected String ofFK;
    protected String onFk;

    {
        findById = "select * from %s where id = ?";
        findAll = "select * from %s";
        deleteByKey = "delete from %s where id = ?";
        connection = Util.getConnection();
        ofFK = "SET FOREIGN_KEY_CHECKS=0";
        onFk = "SET FOREIGN_KEY_CHECKS=1";
    }

    protected CrudSql(String tableName) {
        init(tableName);
    }

    protected void setTableName(String tableName) {
        this.init(tableName);
    }

    public void ofFk() {
        try (Statement offFk = connection.createStatement()) {
            offFk.executeQuery(this.ofFK);
        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
        }
    }
    public void onFk() {
        try (Statement onFk = connection.createStatement()) {
            onFk.executeQuery(this.onFk);
        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
        }
    }
    private void init(String tableName) {
        this.findById = String.format(findById, tableName);
        this.findAll = String.format(findAll, tableName);
        this.deleteByKey = String.format(deleteByKey, tableName);
    }
}
