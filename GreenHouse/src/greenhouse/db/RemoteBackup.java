/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.db;

import greenhouse.dao.GreenhouseDAO;
import greenhouse.model.Greenhouse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Hasitha Lakmal
 */
public class RemoteBackup {

    private static RemoteBackup connection;
    private final Properties prop;
    private final InputStream input;
    private static Connection conn;

    private RemoteBackup() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
        input = new FileInputStream(".\\resources\\db_config.properties");
        prop = new Properties();
        prop.load(input);
        System.out.println("remote_db_server :" + prop.getProperty("db_server"));
        System.out.println("remote_db_name :" + prop.getProperty("db_name"));
        System.out.println("remote_db_user :" + prop.getProperty("db_user"));
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + prop.getProperty("db_server") + "/" + prop.getProperty("db_name"), prop.getProperty("db_user"), prop.getProperty("db_password"));
    }

    private static RemoteBackup getRemoteBackupConnection() throws ClassNotFoundException, SQLException, IOException {
        if (connection == null) {
            connection = new RemoteBackup();
        }
        return connection;

    }
    
    public static void startBackup() throws SQLException, IOException, ClassNotFoundException{
        getRemoteBackupConnection();
        
    }
    
    private static int backupGreenHouses() throws ClassNotFoundException, SQLException, IOException{
        List<Greenhouse> greenHouses = new ArrayList<>();
        greenHouses = GreenhouseDAO.getAllGreenhouses();
        
        List<Greenhouse> remoteGreenhouses = new ArrayList<>();
        String sql = "SELECT * FROM greeenhouse";
        ResultSet data = DatabaseHandler.getData(conn, sql);
        
        return 0;
    }
    
    private static int backupDeviceTypes(){
        return 0;
    }
    
    private static int backupDevices(){
        return 0;
    }
    
    private static int backupData(){
        return 0;
    }
    
    private static int backupUserDetails(){
        return 0;
    }
    
    private static int backupUsers(){
        return 0;
    }
    
    private static int backupCommands(){
        return 0;
    }
}
