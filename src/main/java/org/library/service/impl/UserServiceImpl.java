package org.library.service.impl;

import org.library.model.Role;
import org.library.model.User;
import org.library.repository.abstr.RoleDao;
import org.library.repository.abstr.UserDao;
import org.library.service.abstr.DefaultService;
import org.library.service.abstr.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDao userDao;
    private RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }
    private User uploadRole(User user) {
        Set<Role> roles = new HashSet<>();
        for(Role role : user.getRoles()) {
            roles.add(roleDao.findById(role.getId()));
        }
        user.setRoles(roles);
        return user;
    }
    @Override
    public Optional<User> findById(Long id) {
        Set<Role> roles = new HashSet<>();
        User user = userDao.findById(id);
        if(user != null) {
            uploadRole(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> findAll() {
        List<User> users = userDao.getAll();
        List<User> resultUser = new ArrayList<>();
        for(User u : users) {
            if(u != null) {
                User user = uploadRole(u);
                resultUser.add(user);
            }
        }

        return Optional.of(resultUser);
    }

    @Override
    public Optional<List<User>> findByName(String name) {
        Optional<List<User>> optional = userDao.getByFirstName(name);
        List<User> resultUsers = new ArrayList<>();
        if(optional.isPresent()) {
            for(User u : optional.get()) {
                if(u != null) {
                    resultUsers.add(uploadRole(u));
                }
            }
            return Optional.of(resultUsers);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(User o) {
        if(o == null) {
            return false;
        }
        Role role = roleDao.findByName(o.getRoles().stream().findFirst().get().getName().name());
        o.setRoles(Collections.singleton(role));
        if(userDao.update(o)) return true;
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        if (userDao.deleteByKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean save(User o) {
        Role role = roleDao.findByName(o.getRoles().stream().findFirst().get().getName().name());
        o.setRoles(Collections.singleton(role));
        if(userDao.save(o)) {
            return true;
        }
        return false;

    }

    @Override
    public Optional<User> findByKeyEager(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String name) {
        User user = userDao.findByUsername(name);

        if(user != null) {
            return Optional.of(uploadRole(user));
        }
        return Optional.empty();
    }
}
