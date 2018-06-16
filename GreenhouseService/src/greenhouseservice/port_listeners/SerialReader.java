package greenhouseservice.port_listeners;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import greenhouseservice.dao.DataDAO;
import greenhouseservice.model.Data;
import greenhouseservice.utility.DataDecoder;
import greenhouseservice.view.MainFrame;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * SerialReader provides the capability of reading the data from Serial Port through Serial Port InputStream.
 * @author Hasitha Lakmal
 */
public class SerialReader implements Runnable {

    InputStream in;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    String dataStr = "";

    /**
     * 
     * @param in - SerialPort's InputStream. For GreenhouseService application InputStream is obtained by calling getInputStream() method of gnu.io.SerialPort;
     */
    public SerialReader(InputStream in) {
        this.in = in;
    }

    /**
     * 
     */
    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = this.in.read(buffer)) > -1) {
                String txt = new String(buffer, 0, len);
                if (txt.length() > 0) {
                    dataStr = dataStr + txt;
                    if (dataStr.contains("P")) {
                        System.out.println(dataStr);
                        Data data = DataDecoder.decode(dataStr);
                        if (data != null) {
                            System.out.println("Date : " + dateFormat.format(data.getDate()));
                            System.out.println("Time : " + timeFormat.format(data.getTime()));
                            System.out.println("IP:" + data.getIp() + ","
                                    + "Temp:" + data.getTemperature() + ","
                                    + "Light:" + data.getLight() + ","
                                    + "Humidity:" + data.getHumidity() + ","
                                    + "PH:" + data.getPh() + ","
                                    + "Pressure :" + data.getPressure() + ","
                                    + "Soil Moist :" + data.getSoilMoisture());

                            int res = DataDAO.insertData(data.getIp(), data);
                            if (res > 0) {
                                MainFrame.dataReadStaticLabel.setText("<html>" + dateFormat.format(data.getDate()) + " " + timeFormat.format(data.getTime()) + " Serial Port Input inserted to db.</html>");
                                MainFrame.thisForm.setVisible(true);
                                System.out.println(dateFormat.format(data.getDate()) + " " + timeFormat.format(data.getTime()) + " Serial Port Input inserted to db.");
                            }
                        }
                        dataStr = "";

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e, "Greenhouse Serial Port Listner : Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Serail port connection failed. Try restart Greenhouse Serial Port Listner", "Greenhouse Serial Port Listner : Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerialReader.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "SQL Database connection failed. Try start MySQL database connection in services\n and restart Greenhouse Serial Port Listner", "Greenhouse Serial Port Listner : Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (SQLException ex) {
            Logger.getLogger(SerialReader.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "SQL Database connection failed. Try start MySQL database connection in services\n and restart Greenhouse Serial Port Listner", "Greenhouse Serial Port Listner : Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
