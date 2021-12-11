package org.library.service.impl;

import org.library.repository.abstr.BookDao;
import org.library.repository.abstr.JournalDao;
import org.library.repository.abstr.NewspaperDao;
import org.library.service.abstr.StatisticService;

public class StatisticServiceImpl implements StatisticService {

    private BookDao bookDao;
    private JournalDao journalDao;
    private NewspaperDao newspaperDao;

    public StatisticServiceImpl(BookDao bookDao, JournalDao journalDao, NewspaperDao newspaperDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.newspaperDao = newspaperDao;
    }

    @Override
    public Long getCurrentBookId() {
        return bookDao.getCurrentId();
    }

    @Override
    public Long getCurrentJournalId() {
        return journalDao.getCurrentId();
    }

    @Override
    public Long getCurrentNewspaperId() {
        return newspaperDao.getCurrentId();
    }
}
