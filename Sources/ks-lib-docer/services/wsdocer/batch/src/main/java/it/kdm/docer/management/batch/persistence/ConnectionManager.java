package it.kdm.docer.management.batch.persistence;

import it.kdm.docer.management.batch.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 02/11/15
 * Time: 17.13
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionManager {

    private static Connection conn = null;
    private static String driver;
    private static String url;
    private static String username;
    private static String password;


    public static Connection getLocalConnection() throws Exception {
        if (conn != null && !conn.isClosed()) {
            return conn;
        }
        try {
            driver = Configuration.getInstance().getProperty("rdbms.driver");
            url = Configuration.getInstance().getProperty("rdbms.url");
            username = Configuration.getInstance().getProperty("rdbms.user");
            password = Configuration.getInstance().getProperty("rdbms.password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        }
        catch (SQLException ex) {
            System.out.println("ggg sql exception... = "+ex);
            throw ex;
        } catch (ClassNotFoundException ex) {
            System.out.println("ggg exception  = "+ex);
            throw ex;
        }
    }

    public static void closeConnection() {
        try {
            conn.close();
            conn = null;
            System.out.println("Connessione chiusa....");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
