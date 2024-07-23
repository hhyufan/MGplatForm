package com.miaogu.utils;

import java.sql.*;

public class JDBCTools {
    private static final String url;
    private static final String username;
    private static final String password;
    static {
        //加载mysql8的驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //mysql8的url
        url = "jdbc:mysql://localhost:3306/mgplatform?Unicode=true&characterEncoding=utf8";

        username = "root";
        password = "123456";

    }

    public static Connection getConn() {
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
    public static void closeCoon(Connection coon, PreparedStatement ps, ResultSet rs) throws SQLException {
        if (coon != null) {
            coon.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (rs != null) {
            rs.close();
        }
    }

}
