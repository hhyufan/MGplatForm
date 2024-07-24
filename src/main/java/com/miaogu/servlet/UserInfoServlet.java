package com.miaogu.servlet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miaogu.dao.GlobalDao;
import com.miaogu.dao.GlobalDaoImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    private final GlobalDao globalDao;
    public UserInfoServlet() {
        globalDao = new GlobalDaoImpl();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = getJsonObject(username);
        Gson gson = new Gson();
        String json = gson.toJson(jsonResponse);
        response.getWriter().write(json);
    }

    private JsonObject getJsonObject(String username) {
        JsonObject jsonResponse = new JsonObject();
        List<String> administrators = globalDao.getAdministrator();
        administrators.forEach(
                administrator -> {
                    jsonResponse.addProperty("isAdministrator", administrator.equals(username));
                }
        );
        jsonResponse.addProperty("username", username);
        return jsonResponse;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}
