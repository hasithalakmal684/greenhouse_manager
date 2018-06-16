/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.model;

import java.util.Date;

/**
 *
 * @author Hasitha Lakmal
 */
public class Commands {

    private static final long serialVersionUID = 1L;
    private int comId;
    private String command;
    private int devId;
    private int userId;
    private Date timestamp;

    public Commands() {
    }

    public Commands(Integer comId) {
        this.comId = comId;
    }

    public Commands(Integer comId, String command) {
        this.comId = comId;
        this.command = command;
    }

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getDevId() {
        return devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "greenhouse.model.Commands[ comId=" + comId + " ]";
    }

}
