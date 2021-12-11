package org.library.service.abstr;

import org.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService extends DefaultService<Book> {
    Optional<List<Book>> getByUserId(Long id);
    void addToUser(Long bookId, Long userId);
}
