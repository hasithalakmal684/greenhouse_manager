/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.utility;

import greenhouseservice.model.Data;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class provides the capability of decoding data in a application related data pattern.
 * Pattern - Q;IP address;yyyy-MM-dd hh:mm,val(temperature),val(humidity),val(soil moisture),val(pressure),val(intensity),val(ph),P
 * @author Hasitha Lakmal
 */
public class DataDecoder {

    /**
     * Decode the provided data string which is in application specific pattern.
     * @param dataStr - Text to be decode as a string.
     * @return Data model object with all decoded data.
     */
    public static Data decode(String dataStr) {
        dataStr = dataStr.replaceAll("Q;", "");
        dataStr = dataStr.replaceAll(",P", "");
        String[] split = dataStr.split(";");

        if (split.length == 2) {
            String ip = split[0];
            //Assume 0-temp,1-light,2-humidity,3-pressure,4-ph,5-soil moist
            String[] data = split[1].split(",");
            if (data.length == 7) {

                Data d = new Data();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(data[0]);
                    d.setDate(date);

                    Time time = new Time(dateTimeFormat.parse(data[0]).getTime());
                    d.setTime(time);
                } catch (ParseException ex) {
                    Date date = new Date();
                    d.setDate(date);

                    Time time = new Time(date.getTime());
                    d.setTime(time);
                }
                d.setTemperature(data[1]);
                d.setHumidity(data[2]);
                d.setSoilMoisture(data[3]);
                d.setPressure(data[4]);
                d.setLight(data[5]);
                d.setPh(data[6]);
                d.setIp(ip);
                return d;

            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
