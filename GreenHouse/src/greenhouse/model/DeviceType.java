/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Hasitha Lakmal
 */
public class DeviceType {

    private int dtId;
    private String type;
    private List<Devices> devicesList;
    private Date timestamp;

    public DeviceType() {
    }

    public DeviceType(Integer dtId) {
        this.dtId = dtId;
    }

    public DeviceType(Integer dtId, String type) {
        this.dtId = dtId;
        this.type = type;
    }

    public int getDtId() {
        return dtId;
    }

    public void setDtId(int dtId) {
        this.dtId = dtId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return "greenhouse.model.DeviceType[ dtId=" + dtId + " ]";
    }

}
