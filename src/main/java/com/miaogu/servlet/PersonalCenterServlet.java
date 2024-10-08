package com.miaogu.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.dao.MiaoGuSQLDao;
import com.miaogu.dao.MiaoGuSQLDaoImpl;
import com.miaogu.dao.UserInfoSQLDao;
import com.miaogu.dao.UserInfoSQLDaoImpl;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/personal")
public class PersonalCenterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserInfoSQLDao userInfoSQLDao;
    private final MiaoGuSQLDao miaoGuSQLDao;

    public PersonalCenterServlet() {
        miaoGuSQLDao = new MiaoGuSQLDaoImpl();
        userInfoSQLDao = new UserInfoSQLDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        if (username == null) {
            jsonResponse.addProperty("error", "User not logged in");
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }

        try {
            String email = miaoGuSQLDao.getUserEmail(username);
            if (email == null) {
                jsonResponse.addProperty("error", "Email not found");
            }
            Date registerTime = userInfoSQLDao.getRegisterTime(username);
            if (registerTime == null) {
                jsonResponse.addProperty("error", "Register time not found");
            }


            jsonResponse.addProperty("Email", (email != null) ? email : "fail");
            jsonResponse.addProperty("RegisterTime", (registerTime != null) ? getChineseTime(registerTime) : "undefined");
            String json = gson.toJson(jsonResponse);
            response.getWriter().write(json);
        } catch (SQLException e) {
            jsonResponse.addProperty("error", "Database error: " + e.getMessage());
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    private static String getChineseTime(Date registerTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        // 格式化日期并输出
        return sdf.format(registerTime);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
