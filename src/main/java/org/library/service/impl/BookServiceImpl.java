package org.library.service.impl;

import org.library.model.Author;
import org.library.model.Book;
import org.library.repository.abstr.AuthorDao;
import org.library.repository.abstr.BookDao;
import org.library.service.abstr.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private BookDao bookDao;
    private AuthorDao authorDao;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }


    @Override
    public Optional<Book> findById(Long id) {
        Book book = bookDao.findById(id);
        return Optional.of(uploadAuthor(book));
    }
    private Book uploadAuthor(Book book) {
        if(book != null && book.getAuthor().getId() != null) {
            Author author = authorDao.findById(book.getAuthor().getId());
            book.setAuthor(author);
            return book;
        }
        return null;
    }
    @Override
    public Optional<List<Book>> findAll() {
        List<Book> books = new ArrayList<>();
        for(Book book : bookDao.getAll()) {
            books.add(uploadAuthor(book));
        }
        return Optional.of(books);
    }

    @Override
    public Optional<List<Book>> findByName(String name) {
        List<Book> books = new ArrayList<>();
        Optional<List<Book>> optional = bookDao.getByName(name);
        if(optional.isPresent()) {
            for(Book book : optional.get()) {
                books.add(uploadAuthor(book));
            }
            return Optional.of(books);
        }
        return optional;
    }

    @Override
    public boolean update(Book o) {
        if(o == null) {
            return false;
        }
        if(bookDao.update(o)) return true;
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        if (bookDao.deleteByKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean save(Book o) {
        if(o == null) {
            return false;
        }
        Long id = authorDao.save(o.getAuthor());
        if(id == null) {
            return false;
        }
        o.getAuthor().setId(id);
        o.setDate(LocalDate.now());
        if(bookDao.save(o)) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<Book>> getByUserId(Long id) {
        return bookDao.getByUserId(id);
    }

    @Override
    public void addToUser(Long bookId, Long userId) {
        bookDao.addToUser(bookId, userId);
    }
}
