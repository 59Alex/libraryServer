package org.library.repository.abstr;

import org.library.model.Journal;
import org.library.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends DefaultDao<User> {
    User findByUsername(String name);
    Optional<List<User>> getByFirstName(String name);
}
