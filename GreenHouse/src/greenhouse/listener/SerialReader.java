/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.listener;

import greenhouse.GreenHouse;
import greenhouse.dao.DataDAO;
import greenhouse.model.Data;
import greenhouse.utility.DataDecoder;
import greenhouse.view.NotificationFrame;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hasitha Lakmal
 */
public class SerialReader implements Runnable {

    InputStream in;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    String dataStr = "";

    public SerialReader(InputStream in) {
        this.in = in;
    }

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
                            System.out.println("IP:" + data.getIp() + ","
                                    + "Temp:" + data.getTemperature() + ","
                                    + "Light:" + data.getLight() + ","
                                    + "Humidity:" + data.getHumidity() + ","
                                    + "PH:" + data.getPh() + ","
                                    + "Pressure :" + data.getPressure() + ","
                                    + "Soil Moist :" + data.getSoilMoisture());

                            int res = DataDAO.insertData(data.getIp(), data);
                            if (res > 0) {
                                System.out.println(data.getDate() + " " + data.getTime() + " Serail Port Input inserted to db.");
                                NotificationFrame nf = new NotificationFrame(GreenHouse.listening_port);
                                nf.dataReadStaticLabel.setText("<html>" + dateFormat.format(data.getDate()) + " " + timeFormat.format(data.getTime()) + " Serial Port Input inserted to db.</html>");
                                nf.setVisible(true);
                            }
                        }
                        dataStr = "";

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Serial Port Reading error occurred.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerialReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SerialReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
