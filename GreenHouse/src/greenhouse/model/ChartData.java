/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Hasitha Lakmal
 */
public class ChartData {
    private Date date;
    private Time time;
    private String data;

    public ChartData() {
    }

    public ChartData(Date date, Time time, String data) {
        this.date = date;
        this.time = time;
        this.data = data;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }
    
    @Override
    public String toString(){
        return ChartData.class+" : Date : "+date+", Time : "+time+", Data : "+data;
    }
}
