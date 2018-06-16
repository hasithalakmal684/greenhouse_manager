/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.db.RemoteDatabaseConnection;
import greenhouse.model.Devices;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hasitha Lakmal
 */
public class DeviceDAO {

    private static Connection conn;
    private static Connection remoteConn;

    public static synchronized ArrayList<Devices> getAllDevicesForTypeAndGH(String gh, String type) throws ClassNotFoundException, IOException, SQLException {
        ArrayList<Devices> list = new ArrayList<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT d.dev_id,d.ip_addr "
                + "FROM devices d,device_type dt,greenhouse gh "
                + "WHERE "
                + "d.dt_id IN (select dt2.dt_id from device_type dt2 where type='" + type + "') "
                + "AND "
                + "d.gh_id IN (SELECT g.gh_id from greenhouse g where g.name = '" + gh + "') "
                + "GROUP by d.dev_id";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            Devices d = new Devices(set.getInt("dev_id"), set.getString("ip_addr"));
            list.add(d);
        }

        return list;
    }

    public static int insertNewDeviceList(List<Devices> devicesList) throws ClassNotFoundException, SQLException, IOException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        
        String sql = "INSERT INTO devices(gh_id, dt_id, ip_addr, Description) VALUES ";
        for (Devices device : devicesList) {
            sql = sql + "(" + device.getGhId() + "," + device.getDtId() + ",'" + device.getIpAddr() + "','" + device.getDescription() + "'),";
        }
        sql = sql.substring(0, sql.length() - 1);

        int res = DatabaseHandler.setData(conn, sql);
        
        return res;
    }
    
    public static int insertNewDeviceListRemote(List<Devices> devicesList) throws ClassNotFoundException, SQLException, IOException {
        remoteConn = RemoteDatabaseConnection.getConnection();
        remoteConn.setAutoCommit(false);
        
        String sql = "INSERT INTO devices(gh_id, dt_id, ip_addr, Description) VALUES ";
        for (Devices device : devicesList) {
            sql = sql + "(" + device.getGhId() + "," + device.getDtId() + ",'" + device.getIpAddr() + "','" + device.getDescription() + "'),";
        }
        sql = sql.substring(0, sql.length() - 1);

        int res = DatabaseHandler.setData(remoteConn, sql);
        
        return res;
    }
}
