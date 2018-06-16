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

/**
 *
 * @author Hasitha Lakmal
 */
public class DatabaseConnection {

    private static DatabaseConnection dbConnection;
    private final Properties prop;
    private final InputStream input;
    private final Connection conn;

    private DatabaseConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        input = new FileInputStream(".\\src\\greenhouse\\resources\\db_config.properties");
        prop = new Properties();
        prop.load(input);
        System.out.println("db_server :" + prop.getProperty("db_server"));
        System.out.println("db_name :" + prop.getProperty("db_name"));
        System.out.println("db_user :" + prop.getProperty("db_user"));
//        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://"+prop.getProperty("db_server")+":3306/"+prop.getProperty("db_name"), prop.getProperty("db_user"), prop.getProperty("db_password"));
//        conn = DriverManager.getConnection("jdbc:mysql://localhost/green_house", "root", "");
    }

    private static DatabaseConnection getDBConnection() throws ClassNotFoundException, SQLException, IOException {
        if (dbConnection == null) {
            dbConnection = new DatabaseConnection();
        }
        return dbConnection;

    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        return getDBConnection().conn;
    }

}
