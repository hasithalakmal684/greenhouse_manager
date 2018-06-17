/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasitha Lakmal
 */
public class RemoteDatabaseConnection {

    private static RemoteDatabaseConnection dbConnection;
    private final Properties prop;
    private final InputStream input;
    private final Connection conn;

    private RemoteDatabaseConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        input = new FileInputStream("./resources/db_config.properties");
        prop = new Properties();
        prop.load(input);
        System.out.println("remote_db_server :" + prop.getProperty("remote_db_server"));
        System.out.println("remote_db_name :" + prop.getProperty("remote_db_name"));
        System.out.println("remote_db_user :" + prop.getProperty("remote_db_user"));

//        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("com.mysql.cj.jdbc.Driver");

        conn = DriverManager.getConnection("jdbc:mysql://" + prop.getProperty("remote_db_server") + "/" + prop.getProperty("remote_db_name"), prop.getProperty("remote_db_user"), prop.getProperty("remote_db_password"));
//        conn = DriverManager.getConnection("jdbc:mysql://localhost/green_house", "root", "");
    }

    private static RemoteDatabaseConnection getDBConnection() throws ClassNotFoundException, SQLException, IOException {
        if (dbConnection == null) {
            dbConnection = new RemoteDatabaseConnection();
        }
        return dbConnection;

    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        return getDBConnection().conn;
    }
}
