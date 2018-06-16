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
public class Devices {

    private int devId;
    private String ipAddr;
    private List<Data> dataList;
    private int dtId;
    private int ghId;
    private String description;
    private List<Commands> commandsList;
    private Date timestamp;

    public Devices() {
    }

    public Devices(int devId) {
        this.devId = devId;
    }

    public Devices(int devId, String ipAddr) {
        this.devId = devId;
        this.ipAddr = ipAddr;
    }

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public int getDtId() {
        return dtId;
    }

    public void setDtId(int dtId) {
        this.dtId = dtId;
    }

    public int getGhId() {
        return ghId;
    }

    public void setGhId(int ghId) {
        this.ghId = ghId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Commands> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(List<Commands> commandsList) {
        this.commandsList = commandsList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "greenhouse.model.Devices[ devId=" + devId + " ]";
    }

}
