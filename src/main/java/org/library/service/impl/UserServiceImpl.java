package org.library.service.impl;

import org.library.model.User;
import org.library.repository.abstr.UserDao;
import org.library.service.abstr.DefaultService;
import org.library.service.abstr.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(userDao.findById(id));
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.of(userDao.getAll());
    }

    @Override
    public Optional<User> findByName() {
        return Optional.empty();
    }

    @Override
    public boolean update(User o) {
        if(o == null) {
            return false;
        }
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
        return Optional.of(userDao.findByUsername(name));
    }
}
