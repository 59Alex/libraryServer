package org.library.server;

import org.library.model.Book;
import org.library.model.Newspaper;
import org.library.model.User;
import org.library.repository.impl.AuthorDaoImpl;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.RoleDaoImpl;
import org.library.repository.impl.UserDaoImpl;
import org.library.service.abstr.BookService;
import org.library.service.abstr.UserService;
import org.library.service.impl.BookServiceImpl;
import org.library.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserHandler {
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private static final String getAll = "getAll";
    private static final String update = "update";
    private static final String esc = "esc";
    private static final String save = "save";
    private static final String delete = "delete";
    private static final String findByKey = "findById";
    private static final String findByUsername = "findByUsername";
    private static final String findByName = "findByFirstName";


    public UserHandler(ServerProcess serverProcess) {
        this.userService = new UserServiceImpl(new UserDaoImpl(), new RoleDaoImpl());
        this.serverProcess = serverProcess;
    }

    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if (key == null) {
                continue;
            }
            if (key.equals(getAll)) {
                logger.info("Client запрашивает всех user");
                Optional<List<User>> optional = userService.findAll();
                if (optional.isPresent()) {
                    logger.info("Все users отправлены");
                    ToClient.send(optional.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(update)) {
                logger.info("Client запрашивает обновление user");
                User user = ToClient.accept(serverProcess.getInput());
                if(user != null) {
                    Boolean updateB = userService.update(user);
                    ToClient.send(updateB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(save)) {
                logger.info("Client запрашивает сохранение user");
                User user = ToClient.accept(serverProcess.getInput());
                if(user != null) {
                    Boolean savedB = userService.save(user);
                    ToClient.send(savedB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(delete)) {
                logger.info("Client запрашивает удаление user");
                Long id = ToClient.accept(serverProcess.getInput());
                if(id != null) {
                    Boolean deleted = userService.deleteByKey(id);
                    ToClient.send(deleted, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(findByKey)) {
                logger.info("Client запрашивает user по id");
                Long id = ToClient.accept(serverProcess.getInput());
                Optional<User> newspaper = userService.findById(id);
                if(newspaper.isPresent()) {
                    logger.info("Client отправлен user по id");
                    ToClient.send(newspaper.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(findByUsername)) {
                logger.info("Client запрашивает user по username");
                String name = ToClient.accept(serverProcess.getInput());
                Optional<User> user = userService.findByUsername(name);
                if(user.isPresent()) {
                    logger.info("Client отправлен user по username");
                    ToClient.send(user.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(findByName)) {
                logger.info("Client запрашивает user по first name");
                String name = ToClient.accept(serverProcess.getInput());
                Optional<List<User>> users = userService.findByName(name);
                if(users.isPresent()) {
                    logger.info("Client отправлены users по first name");
                    ToClient.send(users.get(), serverProcess.getOut());
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
