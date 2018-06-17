/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.test;

import greenhouse.db.DatabaseConnection;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Tue Mar 10 02:46:00 IST 1970
 *
 * @author Hasitha Lakmal
 */
public class NewClass {

    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException, ParseException {
        DatabaseConnection.getConnection();
    }
}
