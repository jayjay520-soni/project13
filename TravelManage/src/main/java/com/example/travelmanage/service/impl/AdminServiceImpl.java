package com.example.travelmanage.service.impl;

import com.example.travelmanage.entity.User;
import com.example.travelmanage.mapper.UserMapper;
import com.example.travelmanage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUserList(String username, Integer status) {
        // 直接调用你已有的Mapper方法
        return userMapper.findAll(username, status);
    }

    @Override
    public boolean updateUserStatus(Integer id, Integer status) {
        int rows = userMapper.updateStatus(id, status);
        return rows > 0;
    }
}