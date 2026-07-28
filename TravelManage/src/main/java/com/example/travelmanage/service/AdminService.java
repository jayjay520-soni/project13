package com.example.travelmanage.service;

import com.example.travelmanage.entity.User;
import java.util.List;

public interface AdminService {
    // 查询普通用户列表（支持搜索+状态筛选）
    List<User> getUserList(String username, Integer status);
    // 更新用户状态
    boolean updateUserStatus(Integer id, Integer status);
}