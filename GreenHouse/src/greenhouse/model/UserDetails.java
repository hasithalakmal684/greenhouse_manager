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
public class UserDetails {

    private int userId;
    private String name;
    private String address;
    private String telephone;
    private List<Commands> commandsList;
    private List<Users> usersList;
    private Date timestamp;

    public UserDetails() {
    }

    public UserDetails(Integer userId) {
        this.userId = userId;
    }

    public UserDetails(Integer userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Commands> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(List<Commands> commandsList) {
        this.commandsList = commandsList;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "greenhouse.model.UserDetails[ userId=" + userId + " ]";
    }

}
