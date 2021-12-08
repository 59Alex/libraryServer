package org.library.repository.abstr;

import org.library.model.User;

public interface UserDao extends DefaultDao<User> {
    User findByUsername(String name);
}
