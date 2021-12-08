package org.library.server;

import org.library.model.RoleEnum;
import org.library.model.User;
import org.library.repository.impl.UserDaoImpl;
import org.library.service.abstr.UserService;
import org.library.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthFilter {

    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter() {
        this.userService = new UserServiceImpl(new UserDaoImpl());
    }

    public boolean auth(String username) {
        return userService.findByUsername(username).isPresent();
    }

    public boolean hasRole(String username, RoleEnum roleEnum) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if(optionalUser.isPresent()) {
            return false;
        } else {
            return optionalUser.get().getRoles().contains(roleEnum);
        }
    }

    public void start() {

    }
}
