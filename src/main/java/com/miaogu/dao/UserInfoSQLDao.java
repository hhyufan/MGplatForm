package com.miaogu.dao;

import com.miaogu.utils.MyQueue;

import java.sql.SQLException;
import java.util.Date;

public interface UserInfoSQLDao {
    Date getRegisterTime(String username) throws SQLException;
    void setRegisterTime(String username, Date registerTime) throws SQLException;
    MyQueue<String> getLastLoginTime(String username) throws SQLException;
    void setLastLoginTime(String username, Date lastLoginTime) throws SQLException;
}
