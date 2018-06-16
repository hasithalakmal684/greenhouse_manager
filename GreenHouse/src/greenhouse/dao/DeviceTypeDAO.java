/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.model.DeviceType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hasitha Lakmal
 */
public class DeviceTypeDAO {

    private static Connection conn;

    public static synchronized ArrayList<DeviceType> getAllDeviceTypes() throws ClassNotFoundException, IOException, SQLException {
        ArrayList<DeviceType> list = new ArrayList<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT * FROM device_type";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            DeviceType dt = new DeviceType(set.getInt("dt_id"), set.getString("type"));
            list.add(dt);
        }

        return list;
    }
    
    public static synchronized HashMap<String,Integer> getDeviceTypesMap() throws ClassNotFoundException, IOException, SQLException {
        HashMap<String,Integer> map = new HashMap<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT * FROM device_type";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            map.put(set.getString("type"), set.getInt("dt_id"));
        }

        return map;
    }
    
}
