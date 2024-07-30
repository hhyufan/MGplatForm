package com.miaogu.servlet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.dao.GlobalDao;
import com.miaogu.dao.GlobalDaoImpl;
import com.miaogu.dao.UserInfoSQLDao;
import com.miaogu.dao.UserInfoSQLDaoImpl;
import com.miaogu.utils.GsonQueueConverter;
import com.miaogu.utils.MyQueue;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    private final GlobalDao globalDao;
    private final UserInfoSQLDao userInfoSQLDao;

    public UserInfoServlet() {
        globalDao = new GlobalDaoImpl();
        userInfoSQLDao = new UserInfoSQLDaoImpl();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse;
        try {
            jsonResponse = getJsonObject(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        String json = gson.toJson(jsonResponse);
        response.getWriter().write(json);
    }

    private JsonObject getJsonObject(String username) throws SQLException {
        JsonObject jsonResponse = new JsonObject();
        List<String> administrators = globalDao.getAdministrator();
        MyQueue<String> lastLoginTime = userInfoSQLDao.getLastLoginTime(username);
        if (lastLoginTime == null) {
            lastLoginTime = new MyQueue<>();
        }
        administrators.forEach(
                administrator -> jsonResponse.addProperty("isAdministrator", administrator.equals(username))
        );
        jsonResponse.addProperty("LastLoginTime", GsonQueueConverter.toGsonArray(lastLoginTime));
        jsonResponse.addProperty("username", username);
        return jsonResponse;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}
