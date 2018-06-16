/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.model.UserDetails;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class UserDetailsDAO {
    private static Connection conn;
    
    public static synchronized int insertUserDeatails(UserDetails details) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "INSERT INTO user_details(name, address, telephone) VALUES ('"+details.getName()+"','"+details.getAddress()+"','"+details.getTelephone()+"')";
        int rep = DatabaseHandler.setData(conn, sql);
        
        //0 -> 0 rows affected
        //>0 -> more rows affected
        if(rep > 0){
            sql = "SELECT LAST_INSERT_ID()";
            ResultSet data = DatabaseHandler.getData(conn, sql);
            data.first();
            rep = data.getInt(1);
        }
        return rep;
    }
    
    public static synchronized int deleteUserDeatailsByID(int userId) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "DELETE FROM user_details WHERE user_id="+userId;
        int rep = DatabaseHandler.setData(conn, sql);
        
        return rep;
    }
    
    public static synchronized UserDetails getUserDeatailsByID(int userId) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT * FROM user_details WHERE user_id="+userId;
        ResultSet data = DatabaseHandler.getData(conn, sql);
        UserDetails details = new UserDetails();
        while(data.next()){
            details.setUserId(userId);
            details.setName(data.getString("name"));
            details.setAddress(data.getString("address"));
            details.setTelephone(data.getString("telephone"));
        }
        return details;
    }
}
