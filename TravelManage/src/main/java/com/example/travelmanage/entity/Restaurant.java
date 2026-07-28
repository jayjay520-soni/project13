package com.example.travelmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("restaurant")
public class Restaurant {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;        // 餐厅名称
    private BigDecimal price;   // 人均消费
    private String address;     // 地址
    private String region;      // 地区
    private Double score;        // 评分（0-5）
    private String type;         // 菜系类型
    private Integer sales;       // 销量
    private String description;  // 餐厅介绍
    private String imageUrl;     // 图片链接
}