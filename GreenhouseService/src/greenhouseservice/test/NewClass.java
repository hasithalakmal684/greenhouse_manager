/*
 * Copyright (C) 2018 Hasitha Lakmal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package greenhouseservice.test;

import gnu.io.SerialPort;
import greenhouseservice.port_listeners.SerialPortConnection;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class NewClass {

    public static void main(String[] args) throws SQLException, IOException, Exception {
        System.out.println("COM6");
        SerialPort connection = SerialPortConnection.getConnection("COM6");
        Thread.sleep(2000);
        System.out.println("COM5");
        connection = SerialPortConnection.getConnection("COM5");
    }
}
