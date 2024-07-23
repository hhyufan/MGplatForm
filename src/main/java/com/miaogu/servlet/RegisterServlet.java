package com.miaogu.servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miaogu.dao.MiaoGuSQLDao;
import com.miaogu.dao.MiaoGuSQLDaoImpl;
import com.miaogu.utils.JsonRequestHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final MiaoGuSQLDao miaoGuSQLDao;
    public RegisterServlet() {
        miaoGuSQLDao = new MiaoGuSQLDaoImpl();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理post的请求以及响应的乱码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 读取请求体中的JSON数据
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject jsonRequest = JsonParser.parseString(sb.toString()).getAsJsonObject();
        System.out.println(jsonRequest);
        String username = jsonRequest.get("username").getAsString();
        String password = jsonRequest.get("password").getAsString();
        String email = jsonRequest.get("email").getAsString();
        String type = jsonRequest.get("actionType").getAsString();
        Boolean isEmailExists =  miaoGuSQLDao.isEmailExists(email);
        Boolean isUserExists = miaoGuSQLDao.isUserExists(username);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isUserExists", isUserExists);
        jsonObject.addProperty("isEmailExists", isEmailExists);
        if (Objects.equals(type, "register")) {
            if(!isUserExists && !isEmailExists) {
                try {
                    HttpSession session = request.getSession();
                    miaoGuSQLDao.insertOrUpdateUser(username, password, email);
                    session.setAttribute("username", username);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //使用java代码操作数据
        PrintWriter out = response.getWriter();
        out.println(jsonObject);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request);
        System.out.println(response);
      doGet(request,response);
    }

}
