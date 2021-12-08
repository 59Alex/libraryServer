package org.library.init;

import org.apache.log4j.PropertyConfigurator;
import org.library.MainApplication;

import java.net.URL;

public class InitLogger {

    private final InitLogger initLogger;
    {
        initLogger = new InitLogger();
    }
    static {
        new InitLogger().init("log4j.properties");
    }
    public void init(String s) {
        URL url = getClass().getClassLoader().getResource(s);
        PropertyConfigurator.configure(url);
    }
}
