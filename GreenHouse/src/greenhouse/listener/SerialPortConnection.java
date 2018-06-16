/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.listener;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class SerialPortConnection {

    private final SerialPort port;
    private static SerialPortConnection serialPortConnection;
    private static String portName = "COM5";

    private SerialPortConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new Exception("Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 5000);
            System.out.println("Connected to port :: " + portName);
            if (commPort instanceof SerialPort) {
                port = (SerialPort) commPort;
                port.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } else {
                throw new Exception("Port is not a Serial Port.");
            }
        }
    }

    private static SerialPortConnection getSerialPortConnection(String port) throws ClassNotFoundException, SQLException, IOException, Exception {
        if (serialPortConnection == null) {
            serialPortConnection = new SerialPortConnection();
        } else {
            if (!serialPortConnection.portName.equals(port)) {
                serialPortConnection = new SerialPortConnection();
            }
        }
        return serialPortConnection;

    }

    public static SerialPort getConnection() throws ClassNotFoundException, SQLException, IOException, Exception {
        return getSerialPortConnection(portName).port;
    }

    public static SerialPort getConnection(String port) throws ClassNotFoundException, SQLException, IOException, Exception {
        portName = port;
        return getSerialPortConnection(port).port;
    }
}
