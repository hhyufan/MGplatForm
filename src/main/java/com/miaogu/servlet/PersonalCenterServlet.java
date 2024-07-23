package com.miaogu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
@WebServlet("/PersonalCenterServlet")
public class PersonalCenterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jdbcURL = "jdbc:mysql://localhost:3306/your_database";
        String dbUser = "your_db_user";
        String dbPassword = "your_db_password";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT username, data FROM user_info_register WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, 1); // Assuming you are fetching data for user with id 1
            ResultSet resultSet = statement.executeQuery();

            JsonObject json = new JsonObject();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String data = resultSet.getString("data");

                json.addProperty("username", username);
                json.addProperty("data", data);
            } else {
                json.addProperty("error", "User not found");
            }

            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
