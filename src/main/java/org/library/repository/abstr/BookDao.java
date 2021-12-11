package org.library.repository.abstr;

import org.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao extends DefaultDao<Book> {
    Optional<List<Book>> getByName(String name);
    Long getCurrentId();
    Optional<List<Book>> getByUserId(Long id);
    void addToUser(Long bookId, Long userId);
}
