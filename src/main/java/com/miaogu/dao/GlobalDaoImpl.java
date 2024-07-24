package com.miaogu.dao;

import com.google.gson.Gson;
import com.miaogu.utils.JDBCTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public class GlobalDaoImpl implements GlobalDao{
    public Logger logger = LogManager.getLogger(GlobalDaoImpl.class);
    @Override
    public List<String> getAdministrator() {
        List<String> administrators = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 连接数据库
            conn = JDBCTools.getConn();

            // 2. 准备SQL语句
            String sql = "SELECT data FROM Global WHERE section = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "administrator");

            // 3. 执行查询
            rs = stmt.executeQuery();

            // 4. 处理结果集
            if (rs.next()) {
                // 获取data字段的值
                String jsonData = rs.getString("data");

                // 使用Gson库解析JSON数组
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {}.getType();
                administrators = gson.fromJson(jsonData, listType);
            }

        } catch (SQLException e) {
            logger.error("SQLException occurred in select table Global."); // 实际应用中应该记录日志或者抛出异常
        } finally {
            // 5. 关闭连接和资源
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error("SQLException occurred in close conn."); // 实际应用中应该记录日志或者抛出异常
            }
        }

        return administrators;
    }

}
