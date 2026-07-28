package com.example.travelmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travelmanage.entity.Restaurant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantMapper extends BaseMapper<Restaurant> {
    // 继承 BaseMapper 即可获得 CRUD 方法，无需额外编写
}