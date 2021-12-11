package org.library.repository.impl;

import org.library.model.Newspaper;
import org.library.model.Role;
import org.library.model.User;
import org.library.repository.abstr.UserDao;
import org.library.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class UserDaoImpl extends CrudSql implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String tableName = "user";

    private static final String update;
    private static final String findByName;
    private static final String save;
    private static final String findByFirstName;

    private static final String getRoleIds;
    private static final String updateRole;
    private static final String saveWithRole;
    private static final String currentId;


    private static final String deleteDirectBook;

    private static final String deleteDirectJournal;

    private static final String deleteDirectNewspaper;

    private static final String deleteDirectRole;

    static {
        update = "update user set first_name = ?, last_name = ?, email = ?, username = ?, u_password = ?, enabled = ? where id = ?";
        findByName = "select * from user where username = ?";
        save = "insert into user (first_name, last_name, username, u_password, email, enabled) values(?,?,?,?,?,?)";
        getRoleIds = "select role_id from user_role where user_id = ?";
        findByFirstName = "select * from user where first_name like ?";
        updateRole = "update user_role set role_id = ? where user_id = ?";
        saveWithRole = "insert into user_role (user_id, role_id) values(?,?)";
        currentId = "select max(id) from user";
        deleteDirectBook = "delete from user_book where user_id = ?";
        deleteDirectJournal = "delete from user_journal where user_id = ?";
        deleteDirectNewspaper = "delete from user_newspaper where user_id = ?";
        deleteDirectRole = "delete from user_role where user_id = ?";
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
                .setUsername(resultSet.getString(3))
                .setLastName(resultSet.getString(4))
                .setPassword(resultSet.getString(5))
                .setEmail(resultSet.getString(6))
                .setEnabled(resultSet.getBoolean(7))
                .build();
        int enabled = resultSet.getInt(7);
        if(enabled == 1) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }
        Set<Role> roleIds = getRoleIds(user);
        user.setRoles(roleIds);
        return user;
    }

    private Set<Role> getRoleIds(User user) {
        try(PreparedStatement statement = connection.prepareStatement(getRoleIds)) {
            statement.setLong(1,user.getId());
            ResultSet resultSet = statement.executeQuery();

            Set<Role> list = new HashSet<>();
            while (resultSet.next()) {
                list.add(Role.createBuilder().setId(resultSet.getLong(1)).build());
            }
            return list;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе deleteByKey - {}", super.deleteByKey);
            logger.warn(ex.getMessage());
            return null;
        }
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
        try(PreparedStatement statement = connection.prepareStatement(super.deleteByKey);
            PreparedStatement deleteDirectBookSt = connection.prepareStatement(deleteDirectBook);
            PreparedStatement deleteDirectJournalSt = connection.prepareStatement(deleteDirectJournal);
            PreparedStatement deleteDirectNewspaperSt = connection.prepareStatement(deleteDirectNewspaper);
            PreparedStatement deleteDirectRoleSt = connection.prepareStatement(deleteDirectJournal)) {
                super.ofFk();
                statement.setLong(1, id);
                statement.executeUpdate();
                deleteDirectBookSt.setLong(1, id);
                deleteDirectBookSt.executeUpdate();
                deleteDirectNewspaperSt.setLong(1, id);
                deleteDirectNewspaperSt.executeUpdate();
                deleteDirectJournalSt.setLong(1, id);
                deleteDirectJournalSt.executeUpdate();
                deleteDirectRoleSt.setLong(1, id);
                deleteDirectRoleSt.executeUpdate();
                super.onFk();
                return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе deleteByKey - {}", super.deleteByKey);
            logger.warn(ex.getMessage());
            return false;
        }

    }


    @Override
    public boolean update(User o) {
        logger.info("Запрос на обновление к user - {}", update);

        try(PreparedStatement statement = super.connection.prepareStatement(update);
        PreparedStatement statementForRole = super.connection.prepareStatement(updateRole)) {
            statement.setString(1, o.getFirstName());
            statement.setString(2, o.getLastName());
            statement.setString(3, o.getEmail());
            statement.setString(4, o.getUsername());
            statement.setString(5, o.getPassword());
            if(o.getEnabled()) {
                statement.setInt(6,1);
            } else {
                statement.setInt(6,0);
            }
            statement.setLong(7, o.getId());
            statement.executeUpdate();

            Optional<Role> role = o.getRoles().stream().findFirst();
            if(role.isPresent()) {
                statementForRole.setLong(1, role.get().getId());
                statementForRole.setLong(2, o.getId());
                statementForRole.executeUpdate();
            }

            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе update - {}", update);
            logger.info("Возможно такой email или username же существует");
            logger.warn(ex.getMessage());
            return false;
        }

    }

    @Override
    public boolean save(User o) {
        logger.info("Запрос на сохранение к user по id - {}", save);

        try(PreparedStatement statement = super.connection.prepareStatement(save);
            Statement statementForCurrentUserId = super.connection.createStatement();
        PreparedStatement statementForRole = super.connection.prepareStatement(saveWithRole)) {
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

            ResultSet resultSet = statementForCurrentUserId.executeQuery(currentId);

            if(resultSet.next()) {
                Long lastUserId = resultSet.getLong(1);
                statementForRole.setLong(1, lastUserId);
                statementForRole.setLong(2, o.getRoles().stream().findFirst().get().getId());

                statementForRole.executeUpdate();
            }

            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", save);
            logger.info("Возможно такой email или username же существует");
            logger.warn(ex.getMessage());
            return false;
        }

    }

    @Override
    public User findByUsername(String name) {
        logger.info("Запрос к user по username - {}", findByName);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(findByName)) {
            statement.setString(1,name);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildUser(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно user с username={} не существует.", name);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findByUsername - {}", findByName);
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

    @Override
    public Optional<List<User>> getByFirstName(String name) {
        logger.info("Запрос к user поиск по firstName - {}", findByFirstName);

        try(PreparedStatement statement = super.connection.prepareStatement(findByFirstName)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            List<User> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildUser(resultSet));
            }
            return Optional.of(list);
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе поиск по name - {}", findByFirstName);
            logger.warn(ex.getMessage());
            return Optional.empty();
        }
    }

}
