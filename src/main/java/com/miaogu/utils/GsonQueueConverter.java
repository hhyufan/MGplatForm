package com.miaogu.utils;

import com.google.gson.Gson;

public class GsonQueueConverter {

    /**
     * 将 Gson 字符串转换为队列
     * @param gsonArrayJson Gson 数组的 JSON 字符串
     * @return 转换后的队列
     */
    public static MyQueue<String> fromGsonArray(String gsonArrayJson) {
        Gson gson = new Gson();
        String[] gsonArray = gson.fromJson(gsonArrayJson, String[].class);

        MyQueue<String> queue = new MyQueue<>();
        for (String element : gsonArray) {
            queue.offer(element);
        }

        return queue;
    }

    public static String toGsonArray(MyQueue<String> queue) {
        Gson gson = new Gson();
        String[] gsonArray = queue.toArray(new String[0]);
        return gson.toJson(gsonArray);
    }

}