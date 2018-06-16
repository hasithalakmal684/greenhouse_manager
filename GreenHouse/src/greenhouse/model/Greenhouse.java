/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Hasitha Lakmal
 */
public class Greenhouse {

    private int ghId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<Devices> devicesList;
    private Date timestamp;

    public Greenhouse() {
    }

    public Greenhouse(Integer ghId) {
        this.ghId = ghId;
    }

    public Greenhouse(Integer ghId, String name) {
        this.ghId = ghId;
        this.name = name;
    }

    public int getGhId() {
        return ghId;
    }

    public void setGhId(int ghId) {
        this.ghId = ghId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<Devices> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(List<Devices> devicesList) {
        this.devicesList = devicesList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "greenhouse.model.Greenhouse[ ghId=" + ghId + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
