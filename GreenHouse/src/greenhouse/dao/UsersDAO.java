/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.model.Users;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class UsersDAO {

    private static Connection conn;

    public static synchronized Users getUserAuthenticationDetails(String email) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT * FROM Users where email='" + email + "'";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        Users users = new Users();

        if (set.next()) {
            users.setEmail(email);
            users.setUserId(set.getInt("user_id"));
            users.setPassword(set.getString("password"));
            users.setAltpassword(set.getString("altpassword"));
            users.setUsed(set.getInt("used"));
            users.setId(set.getString("id"));
            users.setAdmin(set.getInt("admin"));
            users.setUserDetails(UserDetailsDAO.getUserDeatailsByID(set.getInt("user_id")));
            return users;
        } else {
            return null;
        }
    }

    public static synchronized int verifyAccount(String email) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "UPDATE users SET used=1 WHERE email='" + email + "'";
        int rep = DatabaseHandler.setData(conn, sql);
        return rep;
    }

    public static synchronized int updateAltPassword(String email, String altPassword) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "UPDATE users SET altpassword='" + altPassword + "',used=0 WHERE email='" + email + "'";
        int rep = DatabaseHandler.setData(conn, sql);
        return rep;
    }
    
    public static synchronized int updatePassword(String email, String password) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "UPDATE users SET password='" + password + "',used=0 WHERE email='" + email + "'";
        int rep = DatabaseHandler.setData(conn, sql);
        return rep;
    }

    public static synchronized int insertUser(Users user) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);

        int lastUserId = UserDetailsDAO.insertUserDeatails(user.getUserDetails());
        if (lastUserId > 0) {
            String sql = "INSERT IGNORE INTO Users (user_id, email, password, id, used, altpassword, admin)\n"
                    + "VALUES (" + lastUserId + ", '" + user.getEmail() + "', '" + user.getPassword() + "','"+user.getId()+"','" + user.getUsed() + "','" + user.getAltpassword() + "'," + user.getAdmin() + ")";
            int rep = DatabaseHandler.setData(conn, sql);
            //0 -> 0 rows affected
            //>0 -> more rows affected
            int r = 0;
            if (rep == 0) {
                r = UserDetailsDAO.deleteUserDeatailsByID(lastUserId);
            }
            return rep;
        } else {
            return lastUserId;
        }
    }
}
