package org.library.repository.abstr;

import java.util.List;

public interface DefaultDao<T> {
    T findById(Long id);
    List<T> getAll();
    boolean deleteByKey(Long id);
    boolean update(T o);
    boolean save(T o);
}
