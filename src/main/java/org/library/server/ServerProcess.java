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
                logger.info("???????????????? ???????????? ?? ???????????? ????????");
                bookHandler.start();
                logger.info("???????????? ????????????");
            }
            if(key.equals(journalKey)) {
                logger.info("???????????????? ???????????? ?? ???????????? journal");
                journalHandler.start();
                logger.info("???????????? ????????????");
            }
            if(key.equals(newspaperKey)) {
                logger.info("???????????????? ???????????? ?? ???????????? newspaper");
                newspaperHandler.start();
                logger.info("???????????? ????????????");
            }
            if(key.equals(userKey)) {
                logger.info("???????????????? ???????????? ?? ???????????? user");
                userHandler.start();
                logger.info("???????????? ????????????");
            }
            if(key.equals(statisticKey)) {
                logger.info("???????????????? ???????????? ?? ???????????? statistic");
                statisticHandler.start();
                logger.info("???????????? ????????????");
            }
            if(key.equals(content)) {
                logger.info(content);
                contentHandler.start();
                logger.info("???????????? ????????????");
            }
        }
        logger.info("Client ????????????????????");
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOut() {
        return out;
    }

}
