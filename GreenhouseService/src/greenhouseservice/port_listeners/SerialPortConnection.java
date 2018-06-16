/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.port_listeners;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Hasitha Lakmal
 */
public class SerialPortConnection {

    private SerialPort port = null;
    private static SerialPortConnection serialPortConnection = null;
    private static String portName = "COM5";

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws Exception throws if Port is currently in use.
     */
    private SerialPortConnection() throws ClassNotFoundException, SQLException, IOException, Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            throw new Exception("Port is currently in use");
        } else {
            if (port != null) {
                port.removeEventListener();
                port.close();
            }
            //open(java.lang.String appname,int timeout)
            System.out.println(this.getClass().getName());
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            System.out.println("Connected to port :: " + portName);
            if (commPort instanceof SerialPort) {
                port = (SerialPort) commPort;
                port.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            } else {
                throw new Exception("Port is not a Serial Port.");
            }
        }
    }

    /**
     * Singleton implementation of SerialPortConnection class
     *
     * @param port - String port name
     * @return SerialPortConnection object
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws Exception throws if Port is currently in use.
     */
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

    /**
     * Get connection to default serial port(COM5). If not available,
     * application will be failed.
     *
     * @return new gnu.io.SerialPort object with connection to default port if
     * serialPortConnection is still null or return already created object with
     * connection to default port.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws Exception throws if Port is currently in use.
     */
    public static SerialPort getConnection() throws ClassNotFoundException, SQLException, IOException, Exception {
        return getSerialPortConnection(portName).port;
    }

    /**
     * Get connection to given serial port. If not available,
     * application will be failed.
     *
     * @param port new gnu.io.SerialPort object with connection to given port if
     * serialPortConnection is still null or have different port name or  return already created object with
     * connection to given port.
     * @return gnu.io.SerialPort object with connection to given port
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws Exception throws if Port is currently in use.
     */
    public static SerialPort getConnection(String port) throws ClassNotFoundException, SQLException, IOException, Exception {
        portName = port;
        return getSerialPortConnection(port).port;
    }
}
