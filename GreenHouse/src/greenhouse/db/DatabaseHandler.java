/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hasitha Lakmal
 */
public class DatabaseHandler {

    private static Statement stm;
    private static ResultSet rst;
    

    public DatabaseHandler() {
    }

    public static int setData(Connection conn, String sql) throws SQLException {
        System.out.println(DatabaseHandler.class+":setData() : " + sql);
        stm = conn.createStatement();
        int res = stm.executeUpdate(sql);
        return res;
    }

    public static ResultSet getData(Connection conn, String sql) throws SQLException {
        System.out.println(DatabaseHandler.class+":getData() : " + sql);
        stm = conn.createStatement();
        rst = stm.executeQuery(sql);
        return rst;
    }
}
