package org.library.server;

import org.library.model.RoleEnum;
import org.library.model.User;
import org.library.repository.impl.RoleDaoImpl;
import org.library.repository.impl.UserDaoImpl;
import org.library.service.abstr.UserService;
import org.library.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthFilter {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private static final String authKey = "auth";

    public AuthFilter(ServerProcess serverProcess) {
        this.userService = new UserServiceImpl(new UserDaoImpl(), new RoleDaoImpl());
        this.serverProcess = serverProcess;
    }

    public User auth(String username, String password) {
        logger.info("Попытка авторизации from client username: {} password: {}", username, password);
        Optional<User> optionalUser = userService.findByUsername(username);
        if(optionalUser.isPresent()) {
            if(optionalUser.get().getPassword().equals(password)) {
                logger.info("User авторизован username: {} password: {}", username, password);
                return optionalUser.get();
            }
        }
        logger.info("User не существует username: {} password: {}", username, password);
        return null;
    }

    public boolean hasRole(String username, RoleEnum roleEnum) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if(optionalUser.isPresent()) {
            return optionalUser.get().getRoles().contains(roleEnum);
        } else {
            return false;
        }
    }

    public void start() {
        while (true) {
            String auth = ToClient.accept(serverProcess.getInput());
            logger.info(auth);
            if(auth == null || auth.equals("")) {
                ToClient.send(null, serverProcess.getOut());
            }

            if(auth.equals(authKey)) {
                String userAndPassword = ToClient.accept(serverProcess.getInput());
                String[] arr = userAndPassword.split(" ");
                if(arr.length == 2) {
                    User user = auth(arr[0], arr[1]);
                    if(user == null) {
                        ToClient.send(user, serverProcess.getOut());
                        continue;
                    } else {
                        ToClient.send(user, serverProcess.getOut());
                        break;
                    }

                }
            }
        }
    }
}
