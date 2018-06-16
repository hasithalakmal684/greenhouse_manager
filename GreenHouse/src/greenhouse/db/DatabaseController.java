/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class DatabaseController {

    public static void backupToSQL() throws IOException, InterruptedException, ClassNotFoundException, SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SHOW TABLES";

        ResultSet set = DatabaseHandler.getData(connection, sql);
        ResultSet subSet;
        while (set.next()) {
            String tableName = set.getString(1);
            sql = "SELECT * FROM " + tableName;
            subSet = DatabaseHandler.getData(connection, sql);
            int columnCnt = subSet.getMetaData().getColumnCount();
            System.out.println("Column Count : " + columnCnt);

            while (subSet.next()) {
                for (int j = 1; j <= columnCnt; j++) {
                    System.out.print(subSet.getObject(j) + " ");
                }
                System.out.println("");
            }
        }
    }

    public static void restoreFromSQL() {
    }
}
