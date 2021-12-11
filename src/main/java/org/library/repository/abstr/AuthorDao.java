package org.library.repository.abstr;

import org.library.model.Author;

import java.util.List;

public interface AuthorDao  {
    Author findById(Long id);
    List<Author> getAll();
    boolean deleteByKey(Long id);
    boolean update(Author o);
    Long save(Author o);
}
