package org.library.server;

import org.library.model.User;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.JournalDaoImpl;
import org.library.repository.impl.NewspaperDaoImpl;
import org.library.service.abstr.StatisticService;
import org.library.service.impl.StatisticServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class StatisticHandler {


    private static final String allQuantityBook = "allQBook";
    private static final String allQuantityJournal = "allQJounal";
    private static final String allQuantityNewspaper = "allQNewspaper";
    private static final String esc = "esc";

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private StatisticService statisticService;

    public StatisticHandler(ServerProcess serverProcess) {
        this.serverProcess = serverProcess;
        this.statisticService = new StatisticServiceImpl(new BookDaoImpl(), new JournalDaoImpl(), new NewspaperDaoImpl());
    }


    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if (key == null) {
                continue;
            }
            if (key.equals(allQuantityBook)) {
                logger.info(allQuantityBook);
                Long q = statisticService.getCurrentBookId();
                if (q != null) {
                    logger.info("Complete");
                    ToClient.send(q, serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if (key.equals(allQuantityJournal)) {
                logger.info(allQuantityJournal);
                Long q = statisticService.getCurrentJournalId();
                if (q != null) {
                    logger.info("Complete");
                    ToClient.send(q, serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if (key.equals(allQuantityNewspaper)) {
                logger.info(allQuantityNewspaper);
                Long q = statisticService.getCurrentNewspaperId();
                if (q != null) {
                    logger.info("Complete");
                    ToClient.send(q, serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(esc)) {
                break;
            }
        }
    }
}
