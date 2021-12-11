package org.library.repository.abstr;

import org.library.model.Book;
import org.library.model.Journal;

import java.util.List;
import java.util.Optional;

public interface JournalDao extends DefaultDao<Journal> {
    Optional<List<Journal>> getByName(String name);
    Long getCurrentId();
    Optional<List<Journal>> getByUserId(Long id);
    void addToUser(Long journalId, Long userId);
}
