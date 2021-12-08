package org.library.service.abstr;

import org.library.model.User;

import java.util.Optional;

public interface UserService extends DefaultService<User>  {
    Optional<User> findByKeyEager(Long id);
    Optional<User> findByUsername(String name);
}
