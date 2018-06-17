/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import greenhouse.db.DatabaseConnection;
import greenhouse.listener.SerialPortConnection;
import greenhouse.listener.SerialReader;
import greenhouse.view.LoginForm;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Hasitha Lakmal
 */
public class GreenHouse extends Thread {

    public static String user_email = "";
    public static String user_name = "";
    public static int user_status = 12;
    public static String listening_port = "COM6";
    private InputStream in;
    public static final String SERIAL_READER_THREAD = "SERIAL_READER_THREAD";
    public static Image app_icon;

    /**
     * @param args the command line arguments
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
//        URL url = ClassLoader.getSystemResource("./images/app.ico");
//        Toolkit kit = Toolkit.getDefaultToolkit();
//        app_icon = kit.createImage(url);
        
        app_icon = ImageIO.read(new File("./images/app.png")).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        
        UIManager.setLookAndFeel(new AcrylLookAndFeel());
        GreenHouse gh = new GreenHouse();
        gh.setName("Main Thread");
        gh.start();

    }

    @Override
    public void run() {
        LoginForm frame = new LoginForm();
        frame.setLocationRelativeTo(null);
        frame.setIconImage(app_icon);
        try {
            DatabaseConnection.getConnection();
            System.out.println("Database connection status : stable");
            Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
            HashMap<String, CommPortIdentifier> portidentifierMap = new HashMap<>();
            while (portIdentifiers.hasMoreElements()) {
                CommPortIdentifier portIdentifier = (CommPortIdentifier) portIdentifiers.nextElement();
                String name = portIdentifier.getName();
                portidentifierMap.put(name, portIdentifier);
            }
            System.out.println("Ports loaded...");
            Object[] toArray = portidentifierMap.keySet().toArray();
            Object selection = JOptionPane.showInputDialog(null, "Select a serial port to start listening.", "Greenhouse Daemon Service Config.", JOptionPane.PLAIN_MESSAGE, null, toArray, null);
            listening_port = (String) selection;
            SerialPort serialPort = SerialPortConnection.getConnection((String) selection);
            in = serialPort.getInputStream();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GreenHouse.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Internal Error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(GreenHouse.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Database Connection Error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(GreenHouse.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Cannot read Database Properties.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(GreenHouse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            frame.setVisible(true);
            if (in != null) {
                Thread t = new Thread(new SerialReader(in));
                t.setName(GreenHouse.SERIAL_READER_THREAD);
                t.start();
                System.out.println("Serial Port Reader start successfull.");
            } else {
                JOptionPane.showMessageDialog(null, "Serail Port Reader doesn't start. Start it manually.");
            }
        }
    }

}
