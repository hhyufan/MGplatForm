package com.miaogu.servlet;

import com.google.gson.JsonObject;
import com.miaogu.dao.MiaoGuSQLDao;
import com.miaogu.dao.MiaoGuSQLDaoImpl;
import com.miaogu.utils.JsonRequestHandler;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final MiaoGuSQLDao miaoGuSQLDao;
    public LoginServlet() {
        miaoGuSQLDao = new MiaoGuSQLDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理post的请求以及响应的乱码
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        JsonObject jsonRequest = JsonRequestHandler.handleJsonRequest(request);
        String username = jsonRequest.get("username").getAsString();
        String password = jsonRequest.get("password").getAsString();
        String type = jsonRequest.get("actionType").getAsString();
        boolean isEmailLogin = jsonRequest.get("isEmailLogin").getAsBoolean();
        Boolean isUserExists ;
        Boolean isEmailExists ;
        String inputPassword;
        JsonObject jsonObject = new JsonObject();
        if(!isEmailLogin) {
            try {isUserExists = miaoGuSQLDao.isUserExists(username);
                inputPassword = miaoGuSQLDao.getUserPassword(username);
                jsonObject.addProperty("isUserExists", isUserExists);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                isEmailExists = miaoGuSQLDao.isEmailExists(username);
                inputPassword = miaoGuSQLDao.getEmailPassword(username);
                jsonObject.addProperty("isEmailExists", isEmailExists);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        if (Objects.equals(type, "login")) {
            HttpSession session = request.getSession();
            jsonObject.addProperty("correctPassword", Objects.equals(inputPassword, password));
            session.setAttribute("username", Objects.equals(inputPassword, password) ? username: null);
        }

        System.out.println(jsonObject);
        // 使用java代码操作数据
        PrintWriter out = response.getWriter();
        out.println(jsonObject);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}