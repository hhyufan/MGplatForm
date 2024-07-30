package com.miaogu.dao;

import com.miaogu.pojo.User;
import com.miaogu.utils.JDBCTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MiaoGuSQLDaoImpl  implements MiaoGuSQLDao{
    @Override
    public  Boolean isUserExists(String username) {
        int count = 0;
        try {
            Connection conn = JDBCTools.getConn();
            String selectSql = "SELECT COUNT(*) AS count FROM User WHERE username = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count > 0;
    }

    @Override
    public void insertOrUpdateUser(String username, String password, String email) throws SQLException {
        Connection conn = JDBCTools.getConn();
        String selectSql;
        // Step 1: 查询是否存在记录

        selectSql = "SELECT COUNT(*) AS count FROM User WHERE username = ?";

        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setString(1, username);
        ResultSet rs = selectStmt.executeQuery();

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }
        if (count > 0) {
            // Step 2: 如果存在记录，则更新
            String updateSql = "UPDATE User SET password = ?, email = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, password);
            updateStmt.setString(2, username);
            updateStmt.setString(3, email);
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("更新成功.");
            } else {
                System.out.println("更新失败...");
            }
            updateStmt.close();
        } else {
            // Step 3: 如果不存在记录，则插入新记录
            String insertSql = "INSERT INTO User(username, password, email) VALUES(?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, email);
            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("插入成功");
            } else {
                System.out.println("插入失败...");
            }
            insertStmt.close();
        }
        conn.close();
    }

    public String getUserPassword(String username) throws SQLException {
        User user = getUserByUsername(username);
        return user != null ? user.getPassWord() : null;
    }

    @Override
    public String getUserEmail(String username) throws SQLException {
        User user = getUserByUsername(username);
        return user != null ? user.getEmail() : null;
    }

    private User getUserByUsername(String username) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        String sql = "SELECT password, email FROM User WHERE username = ?";
        User user = null;

        try {
            conn = JDBCTools.getConn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String passWord = rs.getString("password");
                String email = rs.getString("email");
                System.out.println("username:" + username);
                user = new User(username, passWord, email);
            } else {
                System.out.println("用户不存在: " + username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCTools.closeCoon(conn, ps, rs);
        }

        return user;
    }

    public String getEmailPassword(String email) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT password, username FROM User WHERE email = ?";
        User user = null;
        Connection conn = null;
        try {
            conn = JDBCTools.getConn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                String passWord = rs.getString("password");
                String username = rs.getString("username");
                System.out.println("username:" + email);
                user = new User(username, passWord, email);
            } else {
                System.out.println("邮箱不存在: " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCTools.closeCoon(conn, ps, rs);
        }
        return user != null ? user.getPassWord() : null;
    }
    public Boolean isEmailExists(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        Connection conn;
        conn = JDBCTools.getConn();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                return resultSet.next(); // 如果存在匹配的 Email，返回 true；否则返回 false
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
