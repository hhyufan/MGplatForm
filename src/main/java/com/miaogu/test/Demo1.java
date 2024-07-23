package com.miaogu.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class Demo1 {
    public static void main(String[] args) {
        // 创建一个 Date 对象，这里假设它是 Fri Dec 26 03:05:37 CST 1969
        Date date = new Date(1721746044L * 1000); // 使用 0 表示 Unix 时间戳的起始时间（1970-01-01 00:00:00 UTC）

        // 创建 SimpleDateFormat 对象，指定中国地区和日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        // 设置时区为中国时区（中国标准时间 CST 或者中国东八区的时区 ID）
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        // 格式化日期并输出
        String formattedDate = sdf.format(date);
        System.out.println("Formatted Date (China): " + formattedDate);
    }
}
