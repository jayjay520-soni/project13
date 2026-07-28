package com.example.travelmanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travelmanage.entity.Restaurant;
import com.example.travelmanage.mapper.RestaurantMapper;
import com.example.travelmanage.service.RestaurantService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl extends ServiceImpl<RestaurantMapper, Restaurant> implements RestaurantService {
    // 继承 ServiceImpl 即可实现 CRUD
}