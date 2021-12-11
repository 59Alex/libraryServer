package org.library.server;

import org.library.model.Book;
import org.library.model.Content;
import org.library.model.Journal;
import org.library.model.Newspaper;
import org.library.repository.impl.AuthorDaoImpl;
import org.library.repository.impl.BookDaoImpl;
import org.library.repository.impl.JournalDaoImpl;
import org.library.repository.impl.NewspaperDaoImpl;
import org.library.service.abstr.BookService;
import org.library.service.impl.BookServiceImpl;
import org.library.service.impl.JournalServiceImpl;
import org.library.service.impl.NewspaperServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContentHandler {

    private static final String esc = "esc";
    private static final String getByUserId = "getByUserId";
    private static final String addBookToUser = "addBookToUser";
    private static final String addJournalToUser = "addJournalToUser";
    private static final String addNewspaperToUser = "addNewspaperToUser";
    private final JournalServiceImpl journalService;
    private final NewspaperServiceImpl newspaperService;
    private BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(ContentHandler.class);
    private ServerProcess serverProcess;

    public ContentHandler(ServerProcess serverProcess) {
        this.bookService = new BookServiceImpl(new BookDaoImpl(), new AuthorDaoImpl());
        this.journalService = new JournalServiceImpl(new JournalDaoImpl(), new AuthorDaoImpl());
        this.newspaperService = new NewspaperServiceImpl(new NewspaperDaoImpl(), new AuthorDaoImpl());
        this.serverProcess = serverProcess;
    }

    public void start() {
        while (true) {
            String key = ToClient.accept(serverProcess.getInput());
            if (key == null) {
                continue;
            }
            if(key.equals(esc)) {
                break;
            }
            if (key.equals(getByUserId)) {
                logger.info(getByUserId);
                Long userId = ToClient.accept(serverProcess.getInput());
                if(userId != null) {
                    Optional<List<Book>> optionalBooks = bookService.getByUserId(userId);
                    Optional<List<Journal>> optionalJournals = journalService.getByUserId(userId);
                    Optional<List<Newspaper>> optionalNewspapers = newspaperService.getByUserId(userId);
                    List<Content> contents = new ArrayList<>();
                    contents.addAll(optionalBooks.get());
                    contents.addAll(optionalJournals.get());
                    contents.addAll(optionalNewspapers.get());

                    ToClient.send(contents, serverProcess.getOut());

                }
            }

            if (key.equals(addBookToUser)) {
                logger.info(addBookToUser);
                Long userId = ToClient.accept(serverProcess.getInput());
                Long bookId = ToClient.accept(serverProcess.getInput());
                if(userId != null && bookId != null) {
                    bookService.addToUser(bookId, userId);
                }
            }
            if (key.equals(addJournalToUser)) {
                logger.info(addJournalToUser);
                Long userId = ToClient.accept(serverProcess.getInput());
                Long journalId = ToClient.accept(serverProcess.getInput());
                if(userId != null && journalId != null) {
                    journalService.addToUser(journalId, userId);
                }
            }
            if (key.equals(addNewspaperToUser)) {
                logger.info(addNewspaperToUser);
                Long userId = ToClient.accept(serverProcess.getInput());
                Long newspaperId = ToClient.accept(serverProcess.getInput());
                if(userId != null && newspaperId != null) {
                    newspaperService.addToUser(newspaperId, userId);
                }
            }
        }
    }
}
