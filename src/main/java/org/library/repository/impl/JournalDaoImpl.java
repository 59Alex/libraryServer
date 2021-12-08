package org.library.repository.impl;

import org.library.model.Author;
import org.library.model.Journal;
import org.library.model.Newspaper;
import org.library.repository.abstr.JournalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JournalDaoImpl extends CrudSql implements JournalDao {

    private static final String tableName = "journal";
    private static final Logger logger = LoggerFactory.getLogger(JournalDaoImpl.class);

    private static final String update;
    private static final String findByName;
    private static final String save;

    static {
        update = "update journal set name = ?, description = ?, body = ?, author = ? where id = ?";
        findByName = "select * from journal where name = ?";
        save = "insert into journal (name, description, body, author) values(?,?,?,?)";
    }

    protected JournalDaoImpl() {
        super(tableName);
    }

    private Journal buildJournal(ResultSet resultSet) throws SQLException {
        Journal journal = Journal.createBuilder()
                .setId(resultSet.getLong(1))
                .setName(resultSet.getString(2))
                .setDescription(resultSet.getString(3))
                .setDate(resultSet.getDate(4).toLocalDate())
                .setBody(resultSet.getString(5))
                .setAuthor(Author.createBuilder().setId(resultSet.getLong(6)).build())
                .build();

        return journal;
    }

    @Override
    public Journal findById(Long id) {
        logger.info("Запрос к journal по id - {}", super.findById);

        ResultSet resultSet = null;
        try(PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1,id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return this.buildJournal(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно journal с id={} не существует.", id);
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
    public List<Journal> getAll() {
        logger.info("Запрос к journal - {}", super.findAll);

        try(Statement statement = super.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<Journal> list = new ArrayList<>();

            while(resultSet.next()) {
                list.add(this.buildJournal(resultSet));
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
        logger.info("Запрос на удаление к journal по id - {}", super.deleteByKey);

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
    public boolean update(Journal o) {
        logger.info("Запрос на обновление к journal по id - {}", this.update);

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
    public boolean save(Journal o) {
        logger.info("Запрос на сохранение к journal по id - {}", this.save);

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
