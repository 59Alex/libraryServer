package org.library.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ToClient {

    private Logger logger = LoggerFactory.getLogger(ToClient.class);


    public ToClient() {
    }



    public static synchronized <T> void send(T o, OutputStream out) {
        try {
            ObjectOutput outObj = new ObjectOutputStream(out);
            outObj.writeObject(o);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized  <T> T accept(InputStream in) {

        try {
            ObjectInputStream input = new ObjectInputStream(in);
            T o = (T) input.readObject();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

}
