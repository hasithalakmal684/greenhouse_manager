/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import greenhouseservice.port_listeners.SerialPortConnection;
import greenhouseservice.port_listeners.SerialReader;
import greenhouseservice.view.MainFrame;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class of GreenhouseService
 *
 * @author Hasitha Lakmal
 */
public class GreenhouseService implements Runnable {

    public static String port = "";
    private Enumeration portIdentifiers;
    private HashMap<String, CommPortIdentifier> portidentifierMap;
    private SerialPort serialPort;
    private InputStream in;

    /**
     * Main method of main class
     *
     * @param args the command line arguments
     * @throws javax.swing.UnsupportedLookAndFeelException AcrylLookAndFeel
     * class must be in classpath. Can be added by JTattoo.jar library.
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new AcrylLookAndFeel());
        GreenhouseService service = new GreenhouseService();

        Thread t = new Thread(service);
        t.start();
    }

    @Override
    public void run() {
        portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        portidentifierMap = new HashMap<>();
        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier portIdentifier = (CommPortIdentifier) portIdentifiers.nextElement();
            String name = portIdentifier.getName();
            if (!portIdentifier.isCurrentlyOwned()) {
                portidentifierMap.put(name, portIdentifier);
            }
        }
        Object[] toArray = portidentifierMap.keySet().toArray();
        Object selection = JOptionPane.showInputDialog(null, "Select serial port to start listening.", "Greenhouse Daemon Service Config.", JOptionPane.PLAIN_MESSAGE, null, toArray, null);
        port = (String) selection;
        System.out.println(selection);

        try {
            if (selection != null) {
                serialPort = SerialPortConnection.getConnection(port);
                in = serialPort.getInputStream();
                (new Thread(new SerialReader(in))).start();
                MainFrame frame = new MainFrame(port);
                frame.setVisible(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GreenhouseService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GreenhouseService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GreenhouseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
