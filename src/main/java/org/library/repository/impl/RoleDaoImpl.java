package org.library.repository.impl;

import org.library.model.Role;
import org.library.model.RoleEnum;
import org.library.model.User;
import org.library.repository.abstr.RoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl extends CrudSql implements RoleDao {

    private final static String tableName = "role";
    private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    private static final String update;
    private static final String findByName;
    private static final String save;
    private static final String findRoleByUserId;

    static {
        update = "update role set name = ?, description = ? where id = ?";
        findByName = "select * from role where name = ?";
        save = "insert into role (name, description) values(?,?)";
        findRoleByUserId = "select * from role join user_role on user_role.user_id = ? and role.id = user_role.role_id";
    }

    public RoleDaoImpl() {
        super(tableName);
    }

    @Override
    public Role findById(Long id) {
        logger.info("Запрос к role по id - {}", super.findById);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildRole(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно role с id={} не существует.", id);
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

    private Role buildRole(ResultSet resultSet) throws SQLException {
        Role role = Role.createBuilder()
                .setId(resultSet.getLong(1))
                .setDescription(resultSet.getString(3))
                .build();

        String auth = resultSet.getString(2);
        if(auth.equals(RoleEnum.ADMIN.name())) {
            role.setName(RoleEnum.ADMIN);
        } else if(auth.equals(RoleEnum.USER.name())) {
            role.setName(RoleEnum.USER);
        } else if(auth.equals(RoleEnum.EMPLOYEE.name())) {
            role.setName(RoleEnum.EMPLOYEE);
        }
        return role;
    }

    @Override
    public List<Role> getAll() {
        logger.info("Запрос к role - {}", super.findAll);

        try(Statement statement = super.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<Role> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildRole(resultSet));
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
        logger.info("Запрос на удаление к role по id - {}", super.deleteByKey);

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
    public boolean update(Role o) {
        logger.info("Запрос на обновление к role по id - {}", update);

        try(PreparedStatement statement = super.connection.prepareStatement(update)) {
            statement.setString(1, o.getName().name());
            statement.setString(2, o.getDescription());
            statement.setLong(3, o.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе update - {}", update);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(Role o) {
        logger.info("Запрос на сохранение к role по id - {}", save);

        try(PreparedStatement statement = super.connection.prepareStatement(save)) {
            statement.setString(1, o.getName().name());
            statement.setString(2, o.getDescription());

            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", save);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public Role getRoleByUserId(Long id) {
        logger.info("Запрос к role по user_id - {}", findRoleByUserId);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(findRoleByUserId)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildRole(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно role с id={} не существует.", id);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findByUserId - {}", findRoleByUserId);
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
    public Role findByName(String name) {
        logger.info("Запрос к role по name - {}", findByName);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(findByName)) {
            statement.setString(1,name);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildRole(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно role с name={} не существует.", name);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findById - {}", findByName);
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
