/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.db.RemoteDatabaseConnection;
import greenhouse.model.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Hasitha Lakmal
 */
public class DataDAO {

    private static Connection conn;
    private static Connection remoteConn;

    public static synchronized Data getSummarizedDataForGH(String gh) throws ClassNotFoundException, IOException, SQLException {
        Calendar instance = Calendar.getInstance();
        Date dateTime = instance.getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        int minute = c.get(Calendar.MINUTE) - 5;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:");
        System.out.println(sdf.format(dateTime)+minute);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT COUNT(*) as cnt,AVG(temperature) as temperature ,"
                + "AVG(light) as light,"
                + "AVG(pressure) as pressure,"
                + "AVG(humidity) as humidity,"
                + "AVG(ph) as ph,"
                + "AVG(soil_moisture) as soil_moisture "
                + "FROM data "
                + "WHERE dat_id IN( "
                + "    SELECT dat_id FROM data "
                + "    WHERE dat_logtime > '" + sdf.format(dateTime)+minute + "%' "
                + "    AND "
                + "	dev_id IN( "
                + "        SELECT dev_id "
                + "        FROM devices "
                + "        WHERE gh_id IN( "
                + "        	SELECT gh_id "
                + "            FROM greenhouse "
                + "            WHERE name='" + gh + "' "
                + "        )"
                + "    )"
                + ")";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        Data d = new Data();
        set.first();
        if (set.getInt("cnt") > 0) {
            d.setTemperature(set.getString("temperature"));
            d.setPressure(set.getString("pressure"));
            d.setLight(set.getString("light"));
            d.setHumidity(set.getString("humidity"));
            d.setPh(set.getString("ph"));
            d.setSoilMoisture(set.getString("soil_moisture"));
        } else {
            d.setTemperature("0");
            d.setPressure("0");
            d.setLight("0");
            d.setHumidity("0");
            d.setPh("0");
            d.setSoilMoisture("0");
        }

        return d;
    }

    public static synchronized int insertData(String ip_addr, Data data) throws ClassNotFoundException, SQLException, IOException {
        conn = DatabaseConnection.getConnection();
        remoteConn = RemoteDatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        remoteConn.setAutoCommit(true);
        java.sql.Date date = new java.sql.Date(data.getDate().getTime());
        String sql = "INSERT INTO data(dev_id, date, time, temperature, light, humidity, pressure, ph, soil_moisture) "
                + "SELECT d.dev_id,'" + date + "','" + data.getTime() + "','" + data.getTemperature() + "','" + data.getLight() + "','" + data.getHumidity() + "','" + data.getPressure() + "','" + data.getPh() + "','" + data.getSoilMoisture() + "' "
                + "FROM devices d "
                + "WHERE d.ip_addr='" + ip_addr + "'";

        int res = DatabaseHandler.setData(conn, sql);
        if (res > 0) {
            DatabaseHandler.setData(remoteConn, sql);
        }

        return res;
    }
}
