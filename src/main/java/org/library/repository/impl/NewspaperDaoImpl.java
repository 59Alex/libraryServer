package org.library.repository.impl;

import org.library.model.Author;
import org.library.model.Newspaper;
import org.library.model.Role;
import org.library.model.RoleEnum;
import org.library.repository.abstr.NewspaperDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewspaperDaoImpl extends CrudSql implements NewspaperDao {

    private static final String tableName = "newspaper";
    private static final Logger logger = LoggerFactory.getLogger(NewspaperDaoImpl.class);

    private static final String update;
    private static final String findByName;
    private static final String save;

    static {
        update = "update newspaper set name = ?, description = ?, body = ?, author = ? where id = ?";
        findByName = "select * from newspaper where name = ?";
        save = "insert into newspaper (name, description, body, author) values(?,?,?,?)";
    }

    protected NewspaperDaoImpl() {
        super(tableName);
    }

    private Newspaper buildNewspaper(ResultSet resultSet) throws SQLException {
        Newspaper newspaper = Newspaper.createBuilder()
                .setId(resultSet.getLong(1))
                .setName(resultSet.getString(2))
                .setDescription(resultSet.getString(3))
                .setDate(resultSet.getDate(4).toLocalDate())
                .setBody(resultSet.getString(5))
                .setAuthor(Author.createBuilder().setId(resultSet.getLong(6)).build())
                .build();

        return newspaper;
    }

    @Override
    public Newspaper findById(Long id) {
        logger.info("Запрос к newspaper по id - {}", super.findById);
        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildNewspaper(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно newspaper с id={} не существует.", id);
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

    @Override
    public List<Newspaper> getAll() {
        logger.info("Запрос к newspaper - {}", super.findAll);

        try(Statement statement = super.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<Newspaper> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildNewspaper(resultSet));
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
        logger.info("Запрос на удаление к newspaper по id - {}", super.deleteByKey);

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
    public boolean update(Newspaper o) {
        logger.info("Запрос на обновление к newspaper по id - {}", this.update);

        try(PreparedStatement statement = super.connection.prepareStatement(this.update)) {
            statement.setString(1, o.getName());
            statement.setString(2, o.getDescription());
            statement.setString(3, o.getBody());
            statement.setLong(4, o.getAuthor().getId());
            statement.setLong(5, o.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе update - {}", this.update);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(Newspaper o) {
        logger.info("Запрос на сохранение к newspaper по id - {}", this.save);

        try(PreparedStatement statement = super.connection.prepareStatement(this.save)) {
            statement.setString(1, o.getName());
            statement.setString(2, o.getDescription());
            statement.setString(3, o.getBody());
            statement.setLong(4, o.getAuthor().getId());

            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", this.save);
            logger.warn(ex.getMessage());
            return false;
        }

    }
}
