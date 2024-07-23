package com.miaogu.dao;

import java.sql.SQLException;

public interface MiaoGuSQLDao {
    Boolean isUserExists(String username);

    String getUserPassword(String username) throws SQLException;

    String getEmailPassword(String email) throws SQLException;

    void insertOrUpdateUser(String username, String password , String email) throws SQLException;

    Boolean isEmailExists(String email);
}
