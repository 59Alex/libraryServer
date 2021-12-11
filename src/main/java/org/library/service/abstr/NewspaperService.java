package org.library.service.abstr;

import org.library.model.Book;
import org.library.model.Newspaper;

import java.util.List;
import java.util.Optional;

public interface NewspaperService extends DefaultService<Newspaper> {
    Optional<List<Newspaper>> getByUserId(Long id);
    void addToUser(Long newspaperId, Long userId);
}
