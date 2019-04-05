package com.softnik.noteappfirebase.model;

public class UserModel {

    String nodeAddress;
    String username;
    String email;
    String password;


    public UserModel(String nodeAddress, String username, String email, String password) {
        this.nodeAddress = nodeAddress;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public UserModel() {
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
