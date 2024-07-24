package com.miaogu.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.utils.JDBCTools;
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
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

                // 假设JSON数据格式类似 {"registerTime": "2024-07-23 12:00:00"}
                long registerTimeStr = jsonObject.get("RegisterTime").getAsLong();
                registerTime = new Date(registerTimeStr);
            }
        } catch (SQLException e) {
            logger.error("SQLException", e);
            // 处理数据库异常或日期解析异常
        } finally {
            // 关闭连接，释放资源
            JDBCTools.closeCoon(conn, ps, rs);
        }

        return registerTime;
    }

    @Override
    public void setRegisterTime(String username, Date registerTime) throws SQLException {
        String insertSql = "INSERT INTO user_info_register (username, data) VALUES (?, ?)";
        Connection conn = JDBCTools.getConn();
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, username);

        // 创建一个 JsonObject 并添加 RegisterTime 字段
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("RegisterTime", registerTime.getTime());

        // 将 JsonObject 转换为 JSON 字符串
        Gson gson = new Gson();
        String jsonData = gson.toJson(jsonObject);

        // 将 JSON 字符串作为参数设置到 PreparedStatement 中
        insertStmt.setString(2, jsonData);

        // 执行插入操作
        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败...");
        }

        // 关闭 PreparedStatement 和数据库连接
        insertStmt.close();
        conn.close();
    }
}
