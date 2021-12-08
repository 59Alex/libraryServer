package org.library;


import org.apache.log4j.PropertyConfigurator;

import org.library.model.Role;
import org.library.model.RoleEnum;
import org.library.model.User;
import org.library.repository.abstr.RoleDao;
import org.library.repository.abstr.UserDao;
import org.library.repository.impl.RoleDaoImpl;
import org.library.repository.impl.UserDaoImpl;
import org.library.server.ServerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.net.URL;
import java.util.List;

public class MainApplication  {

    private static Logger logger = LoggerFactory.getLogger(MainApplication.class);


    public static void main(String[] args) {
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.startServer();
    }

}
