package com.example.travelmanage.entity;

import lombok.Data;

@Data
public class ScenicSpot {
    private Integer id;
    private String name;
    private Double price;
    private String address;   // 改为 address
    private String region;
    private Double popularity;// 改为 popularity
    private String level;
    private Integer visitCount;
    private String imageUrl;  // 改为 imageUrl（对应 image_url）
    private String history;   // 数据库如果没有这个字段，可先注释掉
}