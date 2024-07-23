package com.miaogu.pojo;

public class UserInfo {
    private int id;
    private String username;
    private String data;

    public UserInfo(int id, String username, String data) {
        this.id = id;
        this.username = username;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
