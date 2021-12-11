package org.library.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerProcess implements Runnable {

    private Integer id;
    private Socket socket;
    private BufferedReader messageFromServer;
    private BufferedWriter writeMessage;
    private InputStream input;
    private OutputStream out;
    private AuthFilter authFilter;
    private BookHandler bookHandler;
    private JournalHandler journalHandler;
    private NewspaperHandler newspaperHandler;
    private UserHandler userHandler;
    private StatisticHandler statisticHandler;
    private ContentHandler contentHandler;

    private static final Logger logger = LoggerFactory.getLogger(ServerProcess.class);

    private static String bookKey = "book";
    private static String journalKey = "journal";
    private static String newspaperKey = "newspaper";
    private static String userKey = "user";
    private static final String statisticKey = "statistic";
    private static final String content = "content";
    private static final String esc = "esc";

    public ServerProcess(Integer id, Socket socket) {
        this.socket = socket;
        try {
            this.input = socket.getInputStream();
            this.out = socket.getOutputStream();
            this.messageFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writeMessage = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.authFilter = new AuthFilter(this);
            this.bookHandler = new BookHandler(this);
            this.journalHandler = new JournalHandler(this);
            this.newspaperHandler = new NewspaperHandler(this);
            this.userHandler = new UserHandler(this);
            this.statisticHandler = new StatisticHandler(this);
            this.contentHandler = new ContentHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        authFilter.start();
        while (true) {
            String key = ToClient.accept(input);
            if(key == null) {
                continue;
            }
            if(key.equals(esc)) {
                break;
            }
            if(key.equals(bookKey)) {
                logger.info("Поступил запрос в сервис книг");
                bookHandler.start();
                logger.info("Сервис закрыт");
            }
            if(key.equals(journalKey)) {
                logger.info("Поступил запрос в сервис journal");
                journalHandler.start();
                logger.info("Сервис закрыт");
            }
            if(key.equals(newspaperKey)) {
                logger.info("Поступил запрос в сервис newspaper");
                newspaperHandler.start();
                logger.info("Сервис закрыт");
            }
            if(key.equals(userKey)) {
                logger.info("Поступил запрос в сервис user");
                userHandler.start();
                logger.info("Сервис закрыт");
            }
            if(key.equals(statisticKey)) {
                logger.info("Поступил запрос в сервис statistic");
                statisticHandler.start();
                logger.info("Сервис закрыт");
            }
            if(key.equals(content)) {
                logger.info(content);
                contentHandler.start();
                logger.info("Сервис закрыт");
            }
        }
        logger.info("Client отключился");
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOut() {
        return out;
    }

}
