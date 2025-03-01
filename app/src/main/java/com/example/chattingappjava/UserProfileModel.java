package com.example.chattingappjava;

public class UserProfileModel {

    public String userName,userUID;

    public UserProfileModel() {
    }

    public UserProfileModel(String userName, String userUID) {
        this.userName = userName;
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
