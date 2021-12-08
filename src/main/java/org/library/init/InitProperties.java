package org.library.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Этот класс отвечает за инициализацию настроек из файла в classpath
 */
public class InitProperties {

    private static Map<String, String> properties;
    private static Set<String> fileNameProperies;
    private static Logger logger = LoggerFactory.getLogger(InitProperties.class);
    private static InitProperties initProperties = new InitProperties();

    static {
        fileNameProperies = Collections.singleton("server.properties");
        logger.info("Инициализация окружения");
        initProperties.initializer();
    }

    public static synchronized Optional<String> getProperty(String k) {
        if(properties != null) {
            String v = properties.get(k);
            return Optional.of(v);
        }
        logger.warn("Для получения properies проинициализируйте контекст init()");
        return Optional.empty();
    }

    private void initializer() {
        properties = new HashMap<>();
        List<File> files = this.getFiles();
        logger.info("Чтение файлов из CLASSPATH");
        for(File file : files) {
            if(file == null) logger.warn(String.format("Файл %s не найден", file.getName()));

            try(FileReader reader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    String[] keyValueArray = line.split("=",2);
                    if(keyValueArray.length >= 2) {
                        properties.put(keyValueArray[0], keyValueArray[1]);
                    }
                }
            } catch (IOException ex) {
                logger.warn("Ошибка при чтении файла %s", file.getName());
            }
        }
    }
    private List<File> getFiles() {
        ClassLoader classLoader = getClass().getClassLoader();
        List<File> files = new ArrayList<>();
        URL url;
        logger.info("Получение файлов из CLASSPATH");
        for(String name : fileNameProperies) {
            url = classLoader.getResource(name);
            if(url == null) throw new IllegalArgumentException(String.format("Файл с именем %s не найден!", name));
            files.add(new File(url.getFile()));
        }
        return files;
    }
}
