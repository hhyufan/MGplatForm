package com.miaogu.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonRequestHandler {
    public static JsonObject handleJsonRequest(HttpServletRequest request) throws IOException {
        // 读取请求体中的JSON数据
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
        }
        return JsonParser.parseString(sb.toString()).getAsJsonObject();
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        return new JsonParser().parse(sb.toString()).getAsJsonObject();
    }
}
