package org.library.repository.impl;

import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Journal;
import org.library.repository.abstr.BookDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl extends CrudSql implements BookDao {
    private static final String tableName = "book";
    private static final Logger logger = LoggerFactory.getLogger(BookDaoImpl.class);

    private static final String update;
    private static final String findByName;
    private static final String save;
    private static final String currentId;
    private static final String getByUserId;
    private static final String addToUser;

    private static final String deleteDirect;


    static {
        update = "update book set name = ?, description = ?, body = ?, author = ? where id = ?";
        findByName = "select * from book where name like ?";
        save = "insert into book (name, description, body, author, created_date) values(?,?,?,?,?)";
        currentId = "select max(id) from book";
        getByUserId = "select id, name, description, created_date, body, author from book join user_book on book.id = user_book.book_id and user_book.user_id = ?";
        addToUser = "insert into user_book (user_id, book_id) values(?,?)";
        deleteDirect = "delete from user_book where book_id = ?";
    }

    public BookDaoImpl() {
        super(tableName);
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        Book book = Book.createBuilder()
                .setId(resultSet.getLong(1))
                .setName(resultSet.getString(2))
                .setDescription(resultSet.getString(3))
                .setDate(resultSet.getDate(4).toLocalDate())
                .setBody(resultSet.getString(5))
                .setAuthor(Author.createBuilder().setId(resultSet.getLong(6)).build())
                .build();

        return book;
    }

    @Override
    public Book findById(Long id) {
        logger.info("Запрос к book по id - {}", super.findById);

        ResultSet resultSet = null;
        try (PreparedStatement statement = super.connection.prepareStatement(super.findById)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return this.buildBook(resultSet);
            } else {
                logger.info("Ошибка в запросе. Возможно book с id={} не существует.", id);
            }
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе findById - {}", super.findById);
            logger.warn(ex.getMessage());
        } finally {
            if (resultSet != null) {
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
    public List<Book> getAll() {
        logger.info("Запрос к book - {}", super.findAll);

        try (Statement statement = super.connection.createStatement();
             ResultSet resultSet = statement.executeQuery(super.findAll)) {
            List<Book> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(this.buildBook(resultSet));
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
        logger.info("Запрос на удаление к book по id - {}", super.deleteByKey);

        if (this.findById(id) == null) {
            return false;
        }
        try (PreparedStatement statement = connection.prepareStatement(super.deleteByKey);
        PreparedStatement deleteDirectSt = connection.prepareStatement(deleteDirect)) {
            super.ofFk();
            statement.setLong(1, id);
            statement.executeUpdate();
            deleteDirectSt.setLong(1, id);
            deleteDirectSt.executeUpdate();
            super.onFk();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе deleteByKey - {}", super.deleteByKey);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Book o) {
        logger.info("Запрос на обновление к book по id - {}", update);

        try (PreparedStatement statement = super.connection.prepareStatement(update)) {
            statement.setString(1, o.getName());
            statement.setString(2, o.getDescription());
            statement.setString(3, o.getBody());
            statement.setLong(4, o.getAuthor().getId());
            statement.setLong(5, o.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе update - {}", update);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(Book o) {
        logger.info("Запрос на сохранение к book по id - {}", save);

        try (PreparedStatement statement = super.connection.prepareStatement(save)) {
            statement.setString(1, o.getName());
            statement.setString(2, o.getDescription());
            statement.setString(3, o.getBody());
            statement.setLong(4, o.getAuthor().getId());
            statement.setDate(5, Date.valueOf(o.getDate()));

            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе save - {}", save);
            logger.warn(ex.getMessage());
            return false;
        }
    }

    @Override
    public Optional<List<Book>> getByName(String name) {
        logger.info("Запрос к book поиск по имени - {}", findByName);

        try (PreparedStatement statement = super.connection.prepareStatement(findByName)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            List<Book> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(this.buildBook(resultSet));
            }
            return Optional.of(list);
        } catch (SQLException ex) {
            logger.warn("Ошибка в запросе поиск по name - {}", findByName);
            logger.warn(ex.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public Long getCurrentId() {
        logger.info(currentId);
        try (Statement statement = super.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(currentId);
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }

        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public Optional<List<Book>> getByUserId(Long id) {
        logger.info(getByUserId);

        try (PreparedStatement statement = super.connection.prepareStatement(getByUserId)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<Book> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(this.buildBook(resultSet));
            }
            return Optional.of(list);
        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void addToUser(Long bookId, Long userId) {
        logger.info(addToUser);
        try (PreparedStatement statement = super.connection.prepareStatement(addToUser)) {
            statement.setLong(1, userId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.warn(ex.getMessage());
        }
    }
}
