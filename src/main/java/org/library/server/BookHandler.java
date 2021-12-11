package org.library.server;

import org.library.model.Book;
import org.library.repository.impl.AuthorDaoImpl;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.UserDaoImpl;
import org.library.service.abstr.BookService;
import org.library.service.abstr.UserService;
import org.library.service.impl.BookServiceImpl;
import org.library.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class BookHandler {
    private BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private static final String getAll = "getAll";
    private static final String update = "update";
    private static final String esc = "esc";
    private static final String save = "save";
    private static final String delete = "delete";
    private static final String findByKey = "findById";
    private static final String findByName = "findByName";


    public BookHandler(ServerProcess serverProcess) {
        this.bookService = new BookServiceImpl(new BookDaoImpl(), new AuthorDaoImpl());
        this.serverProcess = serverProcess;
    }

    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if(key == null) {
                continue;
            }
            if(key.equals(getAll)) {
                logger.info("Client запрашивает все книги");
                Optional<List<Book>> optional = bookService.findAll();
                if(optional.isPresent()) {
                    logger.info("Все книги отправлены");
                    ToClient.send(optional.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(update)) {
                logger.info("Client запрашивает обновление книги");
                Book book = ToClient.accept(serverProcess.getInput());
                if(book != null) {
                    Boolean updateB = bookService.update(book);
                    ToClient.send(updateB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(save)) {
                logger.info("Client запрашивает сохранение книги");
                Book book = ToClient.accept(serverProcess.getInput());
                if(book != null) {
                    Boolean savedB = bookService.save(book);
                    ToClient.send(savedB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(delete)) {
                logger.info("Client запрашивает удаление книги");
                Long id = ToClient.accept(serverProcess.getInput());
                if(id != null) {
                    Boolean deleted = bookService.deleteByKey(id);
                    ToClient.send(deleted, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(findByKey)) {
                logger.info("Client запрашивает book по id");
                Long id = ToClient.accept(serverProcess.getInput());
                Optional<Book> book = bookService.findById(id);
                if(book.isPresent()) {
                    logger.info("Client отправлена книга по id");
                    ToClient.send(book.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(findByName)) {
                logger.info("Client запрашивает book по id");
                String name = ToClient.accept(serverProcess.getInput());
                Optional<List<Book>> books = bookService.findByName(name);
                if(books.isPresent()) {
                    logger.info("Client отправлены книги по name");
                    ToClient.send(books.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(esc)) {
                return;
            }
        }
    }

}
