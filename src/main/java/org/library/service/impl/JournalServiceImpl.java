package org.library.service.impl;

import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Journal;
import org.library.repository.abstr.AuthorDao;
import org.library.repository.abstr.BookDao;
import org.library.repository.abstr.JournalDao;
import org.library.service.abstr.JournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JournalServiceImpl implements JournalService {

    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private JournalDao journalDao;
    private AuthorDao authorDao;

    public JournalServiceImpl(JournalDao journalDao, AuthorDao authorDao) {
        this.journalDao = journalDao;
        this.authorDao = authorDao;
    }
    @Override
    public Optional<Journal> findById(Long id) {
        Journal journal = journalDao.findById(id);
        return Optional.of(uploadAuthor(journal));
    }
    private Journal uploadAuthor(Journal journal) {
        if(journal != null && journal.getAuthor().getId() != null) {
            Author author = authorDao.findById(journal.getAuthor().getId());
            journal.setAuthor(author);
            return journal;
        }
        return null;
    }

    @Override
    public Optional<List<Journal>> findAll() {
        List<Journal> journals = new ArrayList<>();
        for(Journal journal : journalDao.getAll()) {
            journals.add(uploadAuthor(journal));
        }
        return Optional.of(journals);
    }

    @Override
    public Optional<List<Journal>> findByName(String name) {
        List<Journal> journals = new ArrayList<>();
        Optional<List<Journal>> optional = journalDao.getByName(name);
        if(optional.isPresent()) {
            for(Journal journal : optional.get()) {
                journals.add(uploadAuthor(journal));
            }
            return Optional.of(journals);
        }
        return optional;
    }

    @Override
    public boolean update(Journal o) {
        if(o == null) {
            return false;
        }
        if(journalDao.update(o)) return true;
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        if (journalDao.deleteByKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean save(Journal o) {
        if(o == null) {
            return false;
        }
        Long id = authorDao.save(o.getAuthor());
        if(id == null) {
            return false;
        }
        o.getAuthor().setId(id);
        o.setDate(LocalDate.now());
        if(journalDao.save(o)) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<Journal>> getByUserId(Long id) {
        return journalDao.getByUserId(id);
    }

    @Override
    public void addToUser(Long journalId, Long userId) {
        journalDao.addToUser(journalId, userId);
    }
}
