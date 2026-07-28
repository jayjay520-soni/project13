package com.example.travelmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("homestay")
public class Homestay {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;        // 民宿名称
    private BigDecimal price;   // 每晚价格
    private String address;     // 地址
    private String region;      // 地区
    private Double score;        // 评分（0-5）
    private String type;         // 房型类型（如大床房/亲子房）
    private Integer capacity;    // 可住人数
    private String description;  // 民宿介绍
    private String imageUrl;     // 图片链接
}