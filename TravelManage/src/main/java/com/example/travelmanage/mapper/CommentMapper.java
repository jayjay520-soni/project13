package com.example.travelmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travelmanage.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}