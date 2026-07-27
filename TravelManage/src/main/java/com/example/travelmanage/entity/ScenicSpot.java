package com.example.travelmanage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ScenicSpot {
    private Integer id;
    private String name;          // 景点名称
    private BigDecimal price;     // 票价
    private String address;       // 所在地
    private String region;        // 地区（如：江苏-南京-玄武区）
    private Double popularity;    // 热度（0-1）
    private String level;         // 等级（如：5A景区）
    private Integer visitCount;   // 游量
    private String imageUrl;      // 图片路径
    private String history;       // 文化历史
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
