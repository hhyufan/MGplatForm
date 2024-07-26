package com.miaogu.pojo;

import com.google.gson.Gson;

public class User {

    private String userName;

    private String email;

    private String passWord;
    private Gson Data;
    public User(String userName, String passWord, String email) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
    }

    public Gson getData() {
        return Data;
    }

    public void setData(Gson data) {
        Data = data;
    }

    public User(String userName, String passWord, String email, Gson data) {
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.Data = data;
    }
    public String getUserName() {
        return userName;
    }
    public String getEmail() {
        return email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


}
