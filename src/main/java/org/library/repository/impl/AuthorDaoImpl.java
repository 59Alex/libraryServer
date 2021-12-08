package org.library.repository.impl;

import org.library.model.Author;
import org.library.model.Book;
import org.library.repository.abstr.AuthorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl extends CrudSql implements AuthorDao {

    private static final String tableName = "author";
    private static final Logger logger = LoggerFactory.getLogger(AuthorDaoImpl.class);

    private static final String update;
    private static final String findByName;
    private static final String save;

    static {
        update = "update author set first_name = ?, last_name = ?, description = ? where id = ?";
        findByName = "select * from author where name = ?";
        save = "insert into author (first_name, last_name, description) values(?,?,?,?)";
    }

    protected AuthorDaoImpl() {
        super(tableName);
    }

    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        Author author = Author.createBuilder()
                .setId(resultSet.getLong(1))
                .setFirstName(resultSet.getString(2))
                .setLastName(resultSet.getString(3))
                .setDescription(resultSet.getString(4))
                .build();
        return author;
    }

    @Override
    public Author findById(Long id) {
        logger.info("Запрос к author по id - {}", super.findById);

        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildAuthor(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно author с id={} не существует.", id);
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
    public List<Author> getAll() {
        logger.info("Запрос к author - {}", super.findAll);

        try(Statement statement = super.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<Author> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildAuthor(resultSet));
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
        logger.info("Запрос на удаление к author по id - {}", super.deleteByKey);

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
    public boolean update(Author o) {
        logger.info("Запрос на обновление к author по id - {}", this.update);

        try(PreparedStatement statement = super.connection.prepareStatement(this.update)) {
            statement.setString(1, o.getFirstName());
            statement.setString(2, o.getLastName());
            statement.setString(3, o.getDescription());
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
    public boolean save(Author o) {
        logger.info("Запрос на сохранение к author по id - {}", this.save);

        try(PreparedStatement statement = super.connection.prepareStatement(this.save)) {
            statement.setString(1, o.getFirstName());
            statement.setString(2, o.getLastName());
            statement.setString(3, o.getDescription());

            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", this.save);
            logger.warn(ex.getMessage());
            return false;
        }
    }
}
