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
import greenhouse.model.Greenhouse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Hasitha Lakmal
 */
public class GreenhouseDAO {

    private static Connection conn;
    private static Connection remoteConn;

    public static synchronized ArrayList<Greenhouse> getAllGreenhouses() throws ClassNotFoundException, IOException, SQLException {
        ArrayList<Greenhouse> list = new ArrayList<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT * FROM greenhouse";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            Greenhouse g = new Greenhouse(set.getInt("gh_id"), set.getString("name"));
            g.setLatitude(set.getBigDecimal("latitude"));
            g.setLongitude(set.getBigDecimal("longitude"));
            list.add(g);
        }

        return list;
    }

    public static synchronized int isGreenhouseNameAvailable(String name) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        remoteConn = RemoteDatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        remoteConn.setAutoCommit(true);
        String sql = "SELECT * FROM greenhouse where name='" + name + "'";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        set.last();
        if (set.getRow() > 0) {
            return set.getRow();
        } else {
            set = DatabaseHandler.getData(remoteConn, sql);
            return set.getRow();
        }
    }

    public static synchronized int insertNewGreenhouse(Greenhouse greenhouse) throws ClassNotFoundException, IOException, SQLException {
        conn = DatabaseConnection.getConnection();
        remoteConn = RemoteDatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        remoteConn.setAutoCommit(false);
        String sql = "INSERT INTO greenhouse(name, latitude, longitude) VALUES ('" + greenhouse.getName() + "'," + greenhouse.getLatitude() + "," + greenhouse.getLongitude() + ")";
        int res = DatabaseHandler.setData(conn, sql);
        int rem = 0;
        if (greenhouse.getDevicesList().size() > 0) {
            if (res > 0) {
                rem = DatabaseHandler.setData(remoteConn, sql);
                if (rem > 0) {
                    sql = "SELECT LAST_INSERT_ID()";
                    ResultSet data = DatabaseHandler.getData(conn, sql);
                    data.first();
                    int last_id = data.getInt(1);
                    for (Devices devices : greenhouse.getDevicesList()) {
                        devices.setGhId(last_id);
                    }

                    res = DeviceDAO.insertNewDeviceList(greenhouse.getDevicesList());
                    if (res > 0) {
                        data = DatabaseHandler.getData(remoteConn, sql);
                        data.first();
                        int rem_last_id = data.getInt(1);
                        for (Devices devices : greenhouse.getDevicesList()) {
                            devices.setGhId(rem_last_id);
                        }

                        rem = DeviceDAO.insertNewDeviceListRemote(greenhouse.getDevicesList());
                        if (rem > 0) {
                            conn.commit();
                            remoteConn.commit();
                        } else {
                            conn.rollback();
                            remoteConn.rollback();
                        }

                    } else {
                        conn.rollback();
                        remoteConn.rollback();
                    }
                }

            } else {
                conn.rollback();
                remoteConn.rollback();
            }
        } else {
            conn.rollback();
            remoteConn.rollback();
        }

        return res;
    }
}
