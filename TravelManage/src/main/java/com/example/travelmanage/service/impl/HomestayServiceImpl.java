package com.example.travelmanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travelmanage.entity.Homestay;
import com.example.travelmanage.mapper.HomestayMapper;
import com.example.travelmanage.service.HomestayService;
import org.springframework.stereotype.Service;

@Service
public class HomestayServiceImpl extends ServiceImpl<HomestayMapper, Homestay> implements HomestayService {
}