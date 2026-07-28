package com.example.travelmanage.controller;

import com.example.travelmanage.entity.User;
import com.example.travelmanage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin // 跨域
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 1. 查询用户列表（管理员专用）
    @GetMapping("/users")
    public Map<String, Object> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<User> userList = adminService.getUserList(username, status);
            map.put("code", 1);
            map.put("msg", "查询成功");
            map.put("data", userList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "查询失败：" + e.getMessage());
        }
        return map;
    }

    // 2. 更新用户状态（启用/禁用）
    @PostMapping("/user/status")
    public Map<String, Object> updateUserStatus(
            @RequestBody Map<String, Object> param) {
        Map<String, Object> map = new HashMap<>();
        try {
            Integer id = (Integer) param.get("id");
            Integer status = (Integer) param.get("status");
            boolean success = adminService.updateUserStatus(id, status);
            if (success) {
                map.put("code", 1);
                map.put("msg", "状态更新成功");
            } else {
                map.put("code", 0);
                map.put("msg", "状态更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "更新异常：" + e.getMessage());
        }
        return map;
    }
}