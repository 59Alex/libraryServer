package org.library.repository.abstr;

import org.library.model.Book;
import org.library.model.Newspaper;

import java.util.List;
import java.util.Optional;

public interface NewspaperDao extends DefaultDao<Newspaper> {
    Optional<List<Newspaper>> getByName(String name);
    Long getCurrentId();
    Optional<List<Newspaper>> getByUserId(Long id);
    void addToUser(Long newspaperId, Long userId);
}
