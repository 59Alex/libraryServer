package org.library.server;

import org.library.model.Book;
import org.library.model.Newspaper;
import org.library.repository.impl.AuthorDaoImpl;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.NewspaperDaoImpl;
import org.library.service.abstr.BookService;
import org.library.service.abstr.NewspaperService;
import org.library.service.impl.BookServiceImpl;
import org.library.service.impl.NewspaperServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class NewspaperHandler {
    private NewspaperService newspaperService;
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private ServerProcess serverProcess;
    private static final String getAll = "getAll";
    private static final String update = "update";
    private static final String esc = "esc";
    private static final String save = "save";
    private static final String delete = "delete";
    private static final String findByKey = "findById";
    private static final String findByName = "findByName";


    public NewspaperHandler(ServerProcess serverProcess) {
        this.newspaperService = new NewspaperServiceImpl(new NewspaperDaoImpl(), new AuthorDaoImpl());
        this.serverProcess = serverProcess;
    }

    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if(key == null) {
                continue;
            }
            if(key.equals(getAll)) {
                logger.info("Client запрашивает все newspaper");
                Optional<List<Newspaper>> optional = newspaperService.findAll();
                if(optional.isPresent()) {
                    logger.info("Все newspaper отправлены");
                    ToClient.send(optional.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(update)) {
                logger.info("Client запрашивает обновление newspaper");
                Newspaper newspaper = ToClient.accept(serverProcess.getInput());
                if(newspaper != null) {
                    Boolean updateB = newspaperService.update(newspaper);
                    ToClient.send(updateB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(save)) {
                logger.info("Client запрашивает сохранение newspaper");
                Newspaper newspaper = ToClient.accept(serverProcess.getInput());
                if(newspaper != null) {
                    Boolean savedB = newspaperService.save(newspaper);
                    ToClient.send(savedB, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(delete)) {
                logger.info("Client запрашивает удаление newspaper");
                Long id = ToClient.accept(serverProcess.getInput());
                if(id != null) {
                    Boolean deleted = newspaperService.deleteByKey(id);
                    ToClient.send(deleted, serverProcess.getOut());
                } else {
                    ToClient.send(false, serverProcess.getOut());
                }
            }
            if(key.equals(findByKey)) {
                logger.info("Client запрашивает newspaper по id");
                Long id = ToClient.accept(serverProcess.getInput());
                Optional<Newspaper> newspaper = newspaperService.findById(id);
                if(newspaper.isPresent()) {
                    logger.info("Client отправлена newspaper по id");
                    ToClient.send(newspaper.get(), serverProcess.getOut());
                } else {
                    ToClient.send(null, serverProcess.getOut());
                }
            }
            if(key.equals(findByName)) {
                logger.info("Client запрашивает newspaper по id");
                String name = ToClient.accept(serverProcess.getInput());
                Optional<List<Newspaper>> newspapers = newspaperService.findByName(name);
                if(newspapers.isPresent()) {
                    logger.info("Client отправлены книги по name");
                    ToClient.send(newspapers.get(), serverProcess.getOut());
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
