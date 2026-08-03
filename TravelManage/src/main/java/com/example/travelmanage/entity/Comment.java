package com.example.travelmanage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    private String type;

    @TableField("target_id")
    private Integer targetId;

    private String content;

    private Integer score;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;

    private String username;

}