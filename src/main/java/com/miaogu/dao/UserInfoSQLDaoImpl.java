package com.miaogu.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.utils.GsonQueueConverter;
import com.miaogu.utils.JDBCTools;
import com.miaogu.utils.MyQueue;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserInfoSQLDaoImpl implements UserInfoSQLDao {
    private static final Logger logger = LogManager.getLogger(UserInfoSQLDaoImpl.class);

    @Override
    public Date getRegisterTime(String username) throws SQLException {
        return getDate(username);
    }

    private static Date getDate(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Date registerTime = null;

        try {
            conn = JDBCTools.getConn();
            String sql = "SELECT data FROM user_info_register WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String jsonData = rs.getString("data");
                if (jsonData != null && !jsonData.isEmpty()) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                    if (jsonObject.has("RegisterTime")) {
                        long registerTimeStr = jsonObject.get("RegisterTime").getAsLong();
                        registerTime = new Date(registerTimeStr);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException", e);
        } finally {
            JDBCTools.closeCoon(conn, ps, rs);
        }

        return registerTime;
    }

    @Override
    public void setRegisterTime(String username, Date registerTime) throws SQLException {
        setTime(username, "RegisterTime", String.valueOf(registerTime.getTime()));
    }

    private static void setTime(String username, String key, String time) throws SQLException {
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        try {
            conn = JDBCTools.getConn();

            // Step 1: 检查是否存在记录
            String selectSql = "SELECT * FROM user_info_register WHERE username = ?";
            selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // 如果记录已存在，则执行更新操作
                String jsonData = rs.getString("data");
                JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);

                // 添加或更新指定的键值对
                jsonObject.addProperty(key, time);

                // 执行更新操作
                String updateSql = "UPDATE user_info_register SET data = ? WHERE username = ?";
                updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, jsonObject.toString());
                updateStmt.setString(2, username);
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("更新成功");
                } else {
                    System.out.println("更新失败...");
                }
            } else {
                // 如果记录不存在，则执行插入操作
                String insertSql = "INSERT INTO user_info_register (username, data) VALUES (?, ?)";
                insertStmt = conn.prepareStatement(insertSql);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(key, time);
                String jsonData = jsonObject.toString(); // 转换为 JSON 字符串
                insertStmt.setString(1, username);
                insertStmt.setString(2, jsonData);
                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("插入成功");
                } else {
                    System.out.println("插入失败...");
                }
            }

        } finally {
            // 关闭 PreparedStatement 和数据库连接
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (updateStmt != null) {
                updateStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    @Override
    public MyQueue<String> getLastLoginTime(String username) throws SQLException {
        String lastLoginTimeJson = getLoginDate(username);
        if (lastLoginTimeJson == null || lastLoginTimeJson.isEmpty()) {
            return new MyQueue<>(); // 如果没有找到记录，返回一个新的空队列
        }
        return GsonQueueConverter.fromGsonArray(lastLoginTimeJson);
    }

    private String getLoginDate(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String loginTime = null;
        try {
            conn = JDBCTools.getConn();
            String sql = "SELECT data FROM user_info_register WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String jsonData = rs.getString("data");
                if (jsonData != null && !jsonData.isEmpty()) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                    if (jsonObject.has("LastLoginTime")) {
                        loginTime = jsonObject.get("LastLoginTime").getAsString();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("SQLException", e);
        } finally {
            JDBCTools.closeCoon(conn, ps, rs);
        }

        return loginTime;
    }

    @Override
    public void setLastLoginTime(String username, Date lastLoginTime) throws SQLException {
        MyQueue<String> lastLoginTimeQueue = getLastLoginTime(username);

        // 移除队列中的旧时间（如果有的话）
        if (!lastLoginTimeQueue.isEmpty()) {
            lastLoginTimeQueue.poll();
        }

        // 将新时间加入队列
        lastLoginTimeQueue.add(String.valueOf(lastLoginTime.getTime()));

        // 更新数据库中的时间数据
        setTime(username, "LastLoginTime", GsonQueueConverter.toGsonArray(lastLoginTimeQueue));
    }
}
