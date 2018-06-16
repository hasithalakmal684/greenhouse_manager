/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for handling data insert/update/delete/select queries.
 * @author Hasitha Lakmal
 */
public class DatabaseHandler {

    private static Statement stm;
    private static ResultSet rst;

    public DatabaseHandler() {
    }

    /**
     * Method for inserting/updating/deleting data.
     *
     * @param conn - java.sql.Connection object obtained through DatabaseConnection.getConnection();
     * @param sql - SQL Query string.
     * @return Number of columns affected by data manipulation.
     * @throws SQLException
     */
    public static int setData(Connection conn, String sql) throws SQLException {
        System.out.println(DatabaseHandler.class + ":setData() : " + sql);
        stm = conn.createStatement();
        int res = stm.executeUpdate(sql);
        return res;
    }

    /**
     * Method for selecting data.
     * @param conn - java.sql.Connection object obtained through DatabaseConnection.getConnection();
     * @param sql  - SQL Query string.
     * @return A ResultSet object containing all selected data rows.
     * @throws SQLException
     */
    public static ResultSet getData(Connection conn, String sql) throws SQLException {
        System.out.println(DatabaseHandler.class + ":getData() : " + sql);
        stm = conn.createStatement();
        rst = stm.executeQuery(sql);
        return rst;
    }
}
