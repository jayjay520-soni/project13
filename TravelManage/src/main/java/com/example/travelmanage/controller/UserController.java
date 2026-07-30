package com.example.travelmanage.controller;

import com.example.travelmanage.entity.User;
import com.example.travelmanage.mapper.UserMapper;
import com.example.travelmanage.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects; // 新增导入

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 登录（JWT 正式版）
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        User u = userMapper.findByUsername(user.getUsername());

        if (u == null) {
            map.put("code", 0);
            map.put("msg", "用户不存在");
            return map;
        }

        if (!passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            map.put("code", 0);
            map.put("msg", "密码错误");
            return map;
        }

        // 检查角色是否匹配
        if (!user.getRole().equals(u.getRole())) {
            map.put("code", 0);
            map.put("msg", "角色选择错误");
            return map;
        }

        // 生成 Token
        String token = JwtUtil.generateToken(u.getId().toString(), u.getUsername());

        map.put("code", 1);
        map.put("msg", "登录成功");
        map.put("token", token);
        map.put("role", u.getRole());
        map.put("userId", u.getId()); // ✅ 必须返回 userId
        return map;
    }

    // 注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            map.put("code", 0);
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            map.put("code", 0);
            map.put("msg", "密码不能为空");
            return map;
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            map.put("code", 0);
            map.put("msg", "手机号不能为空");
            return map;
        }
        if (!user.getPhone().matches("^1[3-9]\\d{9}$")) {
            map.put("code", 0);
            map.put("msg", "手机号格式不正确");
            return map;
        }
        // 新增：身份非空校验
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            map.put("code", 0);
            map.put("msg", "身份不能为空");
            return map;
        }

        User exist = userMapper.findByUsername(user.getUsername());
        if (exist != null) {
            map.put("code", 0);
            map.put("msg", "用户名已存在");
            return map;
        }

        try {
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);

            int rows = userMapper.addUser(user);
            if (rows > 0) {
                map.put("code", 1);
                map.put("msg", "注册成功");
            } else {
                map.put("code", 0);
                map.put("msg", "注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "注册异常：" + e.getMessage());
        }

        return map;
    }
}