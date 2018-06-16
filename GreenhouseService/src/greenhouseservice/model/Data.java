/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouseservice.model;

import java.util.Date;
import java.sql.Time;

/**
 * Model class for Data Entity
 *
 * @author Hasitha Lakmal
 */
public class Data {

    private static final long serialVersionUID = 1L;
    private Integer datId;
    private Date date;
    private Time time;
    private String temperature;
    private String light;
    private String humidity;
    private String pressure;
    private String ph;
    private String soilMoisture;
    private Date datLogtime;
    private String ip;

    public Data() {
    }

    /**
     * Get IP address of data input device
     *
     * @return IP Address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set IP address of data input device
     *
     * @param ip - IP address of data input device
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Get dataId
     * @return Integer dataId.
     */
    public Integer getDatId() {
        return datId;
    }

    /**
     * Set dataId
     * @param datId - Integer id. This is not necessary, because dataId is auto increment column in database.
     */
    public void setDatId(Integer datId) {
        this.datId = datId;
    }

    /**
     * Get data insertion date
     * @return java.util.Date object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set data insertion date
     * @param date - java.util.Date object.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get data insertion time
     * @return java.sql.Time object
     */
    public Time getTime() {
        return time;
    }

    /**
     * Set data insertion time
     * @param time - java.sql.Time object
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Get temperature value
     * @return temperature value
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Set temperature value
     * @param temperature - temperature value
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Get Intensity value
     * @return intensity value
     */
    public String getLight() {
        return light;
    }

    /**
     * Set Intensity value
     * @param light -  intensity value
     */
    public void setLight(String light) {
        this.light = light;
    }

    /**
     * Get Humidity value
     * @return humidity value
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * Set Humidity value
     * @param humidity - humidity value
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Get Pressure value
     * @return pressure value
     */
    public String getPressure() {
        return pressure;
    }

    /**
     * Set Pressure value
     * @param pressure - pressure value
     */
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    /**
     * Get PH value
     * @return PH value
     */
    public String getPh() {
        return ph;
    }

    /**
     * Set PH value
     * @param ph - PH value
     */
    public void setPh(String ph) {
        this.ph = ph;
    }

    /**
     * Get Soil Moisture value
     * @return Soil Moisture value
     */
    public String getSoilMoisture() {
        return soilMoisture;
    }

    /**
     * Set Soil Moisture value
     * @param soilMoisture - Soil Moisture value
     */
    public void setSoilMoisture(String soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    /**
     * Get data logging timestamp
     * @return data logging timestamp
     */
    public Date getDatLogtime() {
        return datLogtime;
    }

}
