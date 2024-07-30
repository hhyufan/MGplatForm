package com.miaogu.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.miaogu.dao.MiaoGuSQLDao;
import com.miaogu.dao.MiaoGuSQLDaoImpl;
import com.miaogu.dao.UserInfoSQLDao;
import com.miaogu.dao.UserInfoSQLDaoImpl;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final MiaoGuSQLDao miaoGuSQLDao;
    private final UserInfoSQLDao userInfoSQLDao;

    public LoginServlet() {
        miaoGuSQLDao = new MiaoGuSQLDaoImpl();
        userInfoSQLDao = new UserInfoSQLDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理post的请求以及响应的乱码
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        // 读取请求体中的 JSON 数据
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonString = jsonBuilder.toString();
        logError("Received JSON string: " + jsonString);

        // 解析 JSON 数据
        Gson gson = new Gson();
        JsonObject jsonRequest;
        try {
            jsonRequest = gson.fromJson(jsonString, JsonObject.class);
            logError("Parsed JSON object: " + jsonRequest);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Invalid JSON format");
            response.getWriter().write(jsonObject.toString());
            return;
        }

        // 检查 JSON 属性是否存在
        if (jsonRequest == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Request body is empty or invalid");
            response.getWriter().write(jsonObject.toString());
            return;
        }

        // 获取 JSON 属性值
        String username = getStringFromJson(jsonRequest, "username");
        String password = getStringFromJson(jsonRequest, "password");
        String type = getStringFromJson(jsonRequest, "actionType");
        Boolean isEmailLogin = getBooleanFromJson(jsonRequest);

        // 检查是否有缺少的属性
        if (username == null || password == null || type == null || isEmailLogin == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Missing required JSON properties");
            response.getWriter().write(jsonObject.toString());
            logError("Missing required JSON properties: username=" + username + ", password=" + password + ", actionType=" + type + ", isEmailLogin=" + isEmailLogin);
            return;
        }

        logError("JSON properties: username=" + username + ", password=" + password + ", actionType=" + type + ", isEmailLogin=" + isEmailLogin);

        Boolean isUserExists;
        Boolean isEmailExists;
        String inputPassword;
        JsonObject jsonObject = new JsonObject();

        try {
            if (!isEmailLogin) {
                isUserExists = miaoGuSQLDao.isUserExists(username);
                logError("isUserExists: " + isUserExists);
                inputPassword = miaoGuSQLDao.getUserPassword(username);
                logError("inputPassword: " + inputPassword);
                jsonObject.addProperty("isUserExists", isUserExists);
            } else {
                isEmailExists = miaoGuSQLDao.isEmailExists(username);
                logError("isEmailExists: " + isEmailExists);
                inputPassword = miaoGuSQLDao.getEmailPassword(username);
                logError("inputPassword: " + inputPassword);
                jsonObject.addProperty("isEmailExists", isEmailExists);
            }

            if (Objects.equals(type, "login")) {
                HttpSession session = request.getSession();
                boolean correctPassword = Objects.equals(inputPassword, password);
                logError("correctPassword: " + correctPassword);
                jsonObject.addProperty("correctPassword", correctPassword);

                if (correctPassword) {
                    try {
                        userInfoSQLDao.setLastLoginTime(username, new Date());
                        logError("Set last login time successfully for username: " + username);
                    } catch (SQLException e) {
                        logError("Failed to set last login time for username: " + username, e);
                        throw new RuntimeException(e);
                    }
                    session.setAttribute("username", username);
                    logError("Session attribute 'username' set to: " + username);
                } else {
                    session.setAttribute("username", null);
                    logError("Session attribute 'username' set to null");
                }
            }

            System.out.println(jsonObject);
            // 使用java代码操作数据
            PrintWriter out = response.getWriter();
            out.println(jsonObject);

        } catch (SQLException e) {
            logError("SQLException: " + e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logError("Unexpected exception: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    private String getStringFromJson(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            try {
                return jsonObject.get(key).getAsString();
            } catch (ClassCastException | IllegalStateException e) {
                logError("Invalid value for key: " + key, e);
            }
        } else {
            logError("Missing or null value for key: " + key);
        }
        return null;
    }

    private Boolean getBooleanFromJson(JsonObject jsonObject) {
        if (jsonObject.has("isEmailLogin") && !jsonObject.get("isEmailLogin").isJsonNull()) {
            try {
                return jsonObject.get("isEmailLogin").getAsBoolean();
            } catch (ClassCastException | IllegalStateException e) {
                logError("Invalid value for key: " + "isEmailLogin", e);
            }
        } else {
            logError("Missing or null value for key: " + "isEmailLogin");
        }
        return null;
    }

    private void logError(String message) {
        System.err.println(message);
    }

    private void logError(String message, Throwable t) {
        System.err.println(message);
        t.printStackTrace(System.err);
    }
}