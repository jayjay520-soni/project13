package com.example.travelmanage.common;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp; // 新增时间戳

    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    // 原有：只传数据的成功方法
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 新增：传 状态码 + 消息 + 数据
    public static <T> Result<T> success(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 新增：传 状态码 + 消息（无数据，比如新增/更新成功）
    public static <T> Result<T> success(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    // 原有：错误方法（保持不变）
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}