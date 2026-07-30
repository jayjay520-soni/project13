package com.example.travelmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;

    private String type;
    private Integer targetId;

    private String content;
    private Integer score;
    private LocalDateTime createTime;

    private Integer deleted;

    // 去掉 exist=false
    private String username;

}