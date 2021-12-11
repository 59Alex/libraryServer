package org.library.service.abstr;

import java.util.List;
import java.util.Optional;

public interface DefaultService<T> {
    Optional<T> findById(Long id);
    Optional<List<T>> findAll();
    Optional<List<T>> findByName(String name);
    boolean update(T o);
    boolean deleteByKey(Long id);
    boolean save(T o);
}
