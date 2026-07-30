package com.example.travelmanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travelmanage.entity.Comment;
import com.example.travelmanage.mapper.CommentMapper;
import com.example.travelmanage.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}