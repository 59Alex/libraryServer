package org.library.repository.impl;

import org.library.model.User;
import org.library.repository.abstr.UserDao;
import org.library.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class UserDaoImpl extends CrudSql implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String tableName = "user";

    private static final String update;
    private static final String findByName;
    private static final String save;

    static {
        update = "update user set first_name = ?, last_name = ?, email = ?, username = ?, u_password = ?, enabled = ? where id = ?";
        findByName = "select * from user where username = ?";
        save = "insert into user (first_name, last_name, username, u_password, email, enabled) values(?,?,?,?,?,?)";
    }

    public UserDaoImpl() {
        super(tableName);
    }

    @Override
    public User findById(Long id) {
        logger.info("Запрос к user по id - {}", super.findById);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildUser(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно user с id={} не существует.", id);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findById - {}", super.findById);
            logger.warn(ex.getMessage());
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = User.createBuilder()
                .setId(resultSet.getLong(1))
                .setFirstName(resultSet.getString(2))
                .setLastName(resultSet.getString(3))
                .setUsername(resultSet.getString(4))
                .setPassword(resultSet.getString(5))
                .setEmail(resultSet.getString(6))
                .build();
        int enabled = resultSet.getInt(7);
        if(enabled == 1) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }
        return user;
    }
    @Override
    public List<User> getAll() {
        logger.info("Запрос к user - {}", super.findAll);

        try(Statement statement = super.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<User> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildUser(resultSet));
            }
            return list;

        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе getAll - {}", super.findAll);
            logger.warn(ex.getMessage());
        }
        return null;

    }

    @Override
    public boolean deleteByKey(Long id) {
        logger.info("Запрос на удаление к user по id - {}", super.deleteByKey);

        if(this.findById(id) == null) {
            return false;
        }
        try(PreparedStatement statement = connection.prepareStatement(super.deleteByKey)) {
            statement.setLong(1,id);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе deleteByKey - {}", super.deleteByKey);
            logger.warn(ex.getMessage());
            return false;
        }

    }

    @Override
    public boolean update(User o) {
        logger.info("Запрос на обновление к user по id - {}", this.update);

        try(PreparedStatement statement = super.connection.prepareStatement(this.update)) {
            statement.setString(1, o.getFirstName());
            statement.setString(2, o.getLastName());
            statement.setString(3, o.getEmail());
            statement.setString(4, o.getUsername());
            statement.setString(5, o.getPassword());
            if(o.getEnabled()) {
                statement.setInt(6,1);
            } else {
                statement.setInt(6,1);
            }
            statement.setLong(7, o.getId());

            statement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе update - {}", this.update);
            logger.info("Возможно такой email или username же существует");
            logger.warn(ex.getMessage());
            return false;
        }

    }

    @Override
    public boolean save(User o) {
        logger.info("Запрос на сохранение к user по id - {}", this.save);

        try(PreparedStatement statement = super.connection.prepareStatement(this.save)) {
            statement.setString(1, o.getFirstName());
            statement.setString(2, o.getLastName());
            statement.setString(3, o.getUsername());
            statement.setString(4, o.getPassword());
            statement.setString(5, o.getEmail());
            if(o.getEnabled()) {
                statement.setInt(6,1);
            } else {
                statement.setInt(6,1);
            }

            statement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", this.save);
            logger.info("Возможно такой email или username же существует");
            logger.warn(ex.getMessage());
            return false;
        }

    }

    @Override
    public User findByUsername(String name) {
        logger.info("Запрос к user по username - {}", this.findByName);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(this.findByName)) {
            statement.setString(1,name);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildUser(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно user с username={} не существует.", name);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findByUsername - {}", this.findByName);
            logger.warn(ex.getMessage());
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
