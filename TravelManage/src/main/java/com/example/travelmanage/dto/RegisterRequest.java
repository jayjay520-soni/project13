package com.example.travelmanage.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;    // 用户名
    private String password;    // 密码
    private String confirmPassword; // 确认密码
    private String phone;       // 手机号
}
