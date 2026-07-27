package com.example.travelmanage.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 获取当前时间字符串
    public static String getNow() {
        return LocalDateTime.now().format(FORMATTER);
    }

    // 格式化时间
    public static String format(LocalDateTime time) {
        return time.format(FORMATTER);
    }
}
