package com.miaogu.dao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.utils.JDBCTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoSQLDaoImpl implements UserInfoSQLDao {

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
                String registerTimeStr = jsonObject.get("RegisterTime").getAsString();

                // 解析日期时间字符串为Date对象
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                registerTime = sdf.parse(registerTimeStr);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            // 处理数据库异常或日期解析异常
        } finally {
            // 关闭连接，释放资源
            JDBCTools.closeCoon(conn, ps, rs);
        }

        return registerTime;
    }
}
