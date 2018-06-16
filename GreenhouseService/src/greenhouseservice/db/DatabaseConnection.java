/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for creating connection with MySQL database.
 * @author Hasitha Lakmal
 */
public class DatabaseConnection {

    private static DatabaseConnection dbConnection;
    private final Properties prop;
    private final InputStream input;
    private final Connection conn;

    /**
     * db_config.properties file must be in ./config/db_config.properties
     * location to proper creation of DatabaseConnection object. Connection to
     * the mysql database will be established.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private DatabaseConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        input = new FileInputStream("./config/db_config.properties");
        prop = new Properties();
        prop.load(input);
        System.out.println("db_server :" + prop.getProperty("db_server"));
        System.out.println("db_name :" + prop.getProperty("db_name"));
        System.out.println("db_user :" + prop.getProperty("db_user"));
        System.out.println("db_password :" + prop.getProperty("db_password"));
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + prop.getProperty("db_server") + "/" + prop.getProperty("db_name"), prop.getProperty("db_user"), prop.getProperty("db_password"));
//        conn = DriverManager.getConnection("jdbc:mysql://localhost/green_house", "root", "");
    }

    /**
     * Singleton implementation of DatabaseConnection.
     *
     * @return DatabaseConnection object.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    private static DatabaseConnection getDBConnection() throws ClassNotFoundException, SQLException, IOException {
        if (dbConnection == null) {
            dbConnection = new DatabaseConnection();
        }
        return dbConnection;

    }

    /**
     * Method to get the java.sql.Connection object.
     * @return java.sql.Connection object.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        return getDBConnection().conn;
    }

}
