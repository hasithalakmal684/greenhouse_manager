/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.test;

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
        String  s = String.format("%.0f", new Double("8645.123"));
        System.out.println(s);
    }
}
