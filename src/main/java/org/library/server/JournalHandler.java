package org.library.server;

import org.library.model.Book;
import org.library.model.Journal;
import org.library.repository.impl.AuthorDaoImpl;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.JournalDaoImpl;
import org.library.service.abstr.BookService;
import org.library.service.abstr.JournalService;
import org.library.service.impl.BookServiceImpl;
import org.library.service.impl.JournalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class JournalHandler {
    private JournalService journalService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private static final String getAll = "getAll";
    private static final String update = "update";
    private static final String esc = "esc";
    private static final String save = "save";
    private static final String delete = "delete";
    private static final String findByKey = "findById";
    private static final String findByName = "findByName";


    public JournalHandler(ServerProcess serverProcess) {
        this.journalService = new JournalServiceImpl(new JournalDaoImpl(), new AuthorDaoImpl());
        this.serverProcess = serverProcess;
    }

    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if(key == null) {
                continue;
            }
            if(key.equals(getAll)) {
                logger.info("Client запрашивает все journal");
                Optional<List<Journal>> optional = journalService.findAll();
                if(optional.isPresent()) {
                    logger.info("Все journal отправлены");
                    ToClient.send(optional.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(update)) {
                logger.info("Client запрашивает обновление journal");
                Journal journal = ToClient.accept(serverProcess.getInput());
                if(journal != null) {
                    Boolean updateB = journalService.update(journal);
                    ToClient.send(updateB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(save)) {
                logger.info("Client запрашивает сохранение journal");
                Journal journal = ToClient.accept(serverProcess.getInput());
                if(journal != null) {
                    Boolean savedB = journalService.save(journal);
                    ToClient.send(savedB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(delete)) {
                logger.info("Client запрашивает удаление journal");
                Long id = ToClient.accept(serverProcess.getInput());
                if(id != null) {
                    Boolean deleted = journalService.deleteByKey(id);
                    ToClient.send(deleted, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(findByKey)) {
                logger.info("Client запрашивает journal по id");
                Long id = ToClient.accept(serverProcess.getInput());
                Optional<Journal> journal = journalService.findById(id);
                if(journal.isPresent()) {
                    logger.info("Client отправлена journal по id");
                    ToClient.send(journal.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(findByName)) {
                logger.info("Client запрашивает journal по id");
                String name = ToClient.accept(serverProcess.getInput());
                Optional<List<Journal>> journals = journalService.findByName(name);
                if(journals.isPresent()) {
                    logger.info("Client отправлены journal по name");
                    ToClient.send(journals.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(esc)) {
                return;
            }
        }
    }

}
