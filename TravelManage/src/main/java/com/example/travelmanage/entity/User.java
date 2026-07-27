package com.example.travelmanage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime createTime;    // 已存在：创建时间
    private LocalDateTime lastLoginTime; // 新增：最近登录时间
    private Integer status;
    private String phone;
}
