/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.dao;

import greenhouseservice.db.DatabaseConnection;
import greenhouseservice.db.DatabaseHandler;
import greenhouseservice.model.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for insert/update/delete/select Data Entity related queries and data.
 * @author Hasitha Lakmal
 */
public class DataDAO {

    private static Connection conn;

    /**
     *
     * @param ip_addr - IP address of data input device as a string.
     * @param data - Data input of device as greenhouses.model.Data object
     * @return Number of columns affected by data insertion. If data inserted, returns 1 or more. If not returns 0.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public static synchronized int insertData(String ip_addr, Data data) throws ClassNotFoundException, SQLException, IOException {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        java.sql.Date date = new java.sql.Date(data.getDate().getTime());
        String sql = "INSERT INTO data(dev_id, date, time, temperature, light, humidity, pressure, ph, soil_moisture) "
                + "SELECT d.dev_id,'" + date + "','" + data.getTime() + "','" + data.getTemperature() + "','" + data.getLight() + "','" + data.getHumidity() + "','" + data.getPressure() + "','" + data.getPh() + "','" + data.getSoilMoisture() + "' "
                + "FROM devices d "
                + "WHERE d.ip_addr='" + ip_addr + "'";

        int res = DatabaseHandler.setData(conn, sql);

        return res;
    }
}
