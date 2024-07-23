package com.miaogu.dao;

import java.sql.SQLException;
import java.util.Date;

public interface UserInfoSQLDao {
    Date getRegisterTime(String username) throws SQLException;
}
