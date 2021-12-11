package org.library.service.abstr;

import org.library.model.Book;
import org.library.model.Journal;

import java.util.List;
import java.util.Optional;

public interface JournalService extends DefaultService<Journal> {
    Optional<List<Journal>> getByUserId(Long id);
    void addToUser(Long journalId, Long userId);
}
