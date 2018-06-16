/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.model;

import java.util.Date;
import java.sql.Time;

/**
 *
 * @author Hasitha Lakmal
 */
public class Data {

    private int datId;
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

    public Data(Integer datId) {
        this.datId = datId;
    }

    public Data(Integer datId, Date date, Time time, Date datLogtime) {
        this.datId = datId;
        this.date = date;
        this.time = time;
        this.datLogtime = datLogtime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getDatId() {
        return datId;
    }

    public void setDatId(Integer datId) {
        this.datId = datId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getTemperature() {
        return String.format("%.0f", new Double(temperature));
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLight() {
        return String.format("%.0f", new Double(light));
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getHumidity() {
        return String.format("%.0f", new Double(humidity));
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return String.format("%.0f", new Double(pressure));
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPh() {
        return String.format("%.0f", new Double(ph));
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSoilMoisture() {
        return String.format("%.0f", new Double(soilMoisture));
    }

    public void setSoilMoisture(String soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public Date getDatLogtime() {
        return datLogtime;
    }

    public void setDatLogtime(Date datLogtime) {
        this.datLogtime = datLogtime;
    }

    @Override
    public String toString() {
        return "greenhouse.model.Data[ datId=" + datId + " ]";
    }

}
