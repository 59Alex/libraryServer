package org.library.service.impl;

import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Journal;
import org.library.model.Newspaper;
import org.library.repository.abstr.AuthorDao;
import org.library.repository.abstr.JournalDao;
import org.library.repository.abstr.NewspaperDao;
import org.library.service.abstr.NewspaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewspaperServiceImpl implements NewspaperService {

    private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private NewspaperDao newspaperDao;
    private AuthorDao authorDao;

    public NewspaperServiceImpl(NewspaperDao newspaperDao, AuthorDao authorDao) {
        this.newspaperDao = newspaperDao;
        this.authorDao = authorDao;
    }
    @Override
    public Optional<Newspaper> findById(Long id) {
        Newspaper newspaper = newspaperDao.findById(id);
        return Optional.of(uploadAuthor(newspaper));
    }

    private Newspaper uploadAuthor(Newspaper newspaper) {
        if(newspaper != null && newspaper.getAuthor().getId() != null) {
            Author author = authorDao.findById(newspaper.getAuthor().getId());
            newspaper.setAuthor(author);
            return newspaper;
        }
        return null;
    }

    @Override
    public Optional<List<Newspaper>> findAll() {
        List<Newspaper> newspapers = new ArrayList<>();
        for(Newspaper newspaper : newspaperDao.getAll()) {
            newspapers.add(uploadAuthor(newspaper));
        }
        return Optional.of(newspapers);
    }

    @Override
    public Optional<List<Newspaper>> findByName(String name) {
        List<Newspaper> newspapers = new ArrayList<>();
        Optional<List<Newspaper>> optional = newspaperDao.getByName(name);
        if(optional.isPresent()) {
            for(Newspaper newspaper : optional.get()) {
                newspapers.add(uploadAuthor(newspaper));
            }
            return Optional.of(newspapers);
        }
        return optional;
    }

    @Override
    public boolean update(Newspaper o) {
        if(o == null) {
            return false;
        }
        if(newspaperDao.update(o)) return true;
        return false;
    }

    @Override
    public boolean deleteByKey(Long id) {
        if (newspaperDao.deleteByKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean save(Newspaper o) {
        if(o == null) {
            return false;
        }
        Long id = authorDao.save(o.getAuthor());
        if(id == null) {
            return false;
        }
        o.getAuthor().setId(id);
        o.setDate(LocalDate.now());
        if(newspaperDao.save(o)) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<Newspaper>> getByUserId(Long id) {
        List<Newspaper> newspapers = new ArrayList<>();
        Optional<List<Newspaper>> optional = newspaperDao.getByUserId(id);
        if(optional.isPresent()) {
            for(Newspaper newspaper : optional.get()) {
                newspapers.add(uploadAuthor(newspaper));
            }
            return Optional.of(newspapers);
        }
        return optional;
    }

    @Override
    public void addToUser(Long newspaperId, Long userId) {
        newspaperDao.addToUser(newspaperId,userId);
    }
}
