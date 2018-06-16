/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.dao;

import greenhouse.db.DatabaseConnection;
import greenhouse.db.DatabaseHandler;
import greenhouse.model.ChartData;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author Hasitha Lakmal
 */
public class ChartDataDAO {

    private static Connection conn;

    public static synchronized ArrayList<ChartData> getChartData(String dataColumn) throws ClassNotFoundException, IOException, SQLException {
        ArrayList<ChartData> list = new ArrayList<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT " + dataColumn + ",date,time FROM data";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            Date d = set.getDate("date");
            Time t = set.getTime("time");
            String temp = set.getString(dataColumn);
            ChartData tdm = new ChartData(d, t, temp);
            list.add(tdm);
        }

        return list;
    }

    public static synchronized ArrayList<ChartData> getChartDataFromTo(String dataColumn, String ghName, String from, String to) throws ClassNotFoundException, IOException, SQLException {
        ArrayList<ChartData> list = new ArrayList<>();
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(true);
        String sql = "SELECT "+dataColumn+",date,time FROM data \n"
                + "WHERE dev_id IN (\n"
                + "	SELECT d.dev_id FROM devices d WHERE d.gh_id = (\n"
                + "    	SELECT g.gh_id FROM greenhouse g WHERE g.name='"+ghName+"'\n"
                + "    )\n"
                + ") and dat_logtime BETWEEN '"+from+"' AND '"+to+"'";
        ResultSet set = DatabaseHandler.getData(conn, sql);
        while (set.next()) {
            Date d = set.getDate("date");
            Time t = set.getTime("time");
            String temp = set.getString(dataColumn);
            ChartData tdm = new ChartData(d, t, temp);
            list.add(tdm);
        }

        return list;
    }

}
