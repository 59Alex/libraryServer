package org.library.service.abstr;

import java.util.List;
import java.util.Optional;

public interface DefaultService<T> {
    Optional<T> findById(Long id);
    Optional<List<T>> findAll();
    Optional<T> findByName();
    boolean update(T o);
    boolean deleteByKey(Long id);
    boolean save(T o);
}
