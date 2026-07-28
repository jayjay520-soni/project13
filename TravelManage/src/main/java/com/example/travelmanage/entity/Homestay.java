package com.example.travelmanage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Homestay {
    private Integer id;
    private String name;
    private String type;
    private BigDecimal price;
    private String address;
    private String region;
    private Double rating;
    private Integer roomCount;
    private String imageUrl;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
