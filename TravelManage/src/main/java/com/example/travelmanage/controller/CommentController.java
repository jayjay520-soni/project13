package com.example.travelmanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travelmanage.common.Result;
import com.example.travelmanage.entity.Comment;
import com.example.travelmanage.service.CommentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin/comment")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    public CommentController(CommentService commentService, ObjectMapper objectMapper) {
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    // 新增评论
    @PostMapping("/add")
    public Result<Comment> add(@RequestBody Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        boolean success = commentService.save(comment);
        return success ? Result.success(comment) : Result.error(500, "评论失败");
    }

    // 评论列表
    @GetMapping("/list")
    public Result<Page<Comment>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer targetId) {

        Page<Comment> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(content != null && !content.isEmpty(), Comment::getContent, content);
        wrapper.eq(type != null && !type.isEmpty(), Comment::getType, type);
        wrapper.eq(targetId != null, Comment::getTargetId, targetId);

        wrapper.orderByDesc(Comment::getCreateTime);
        commentService.page(pageInfo, wrapper);

        return Result.success(pageInfo);
    }

    // 删除评论
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id, HttpServletRequest request) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return Result.error(400, "评论不存在");
        }

        String userJson = request.getHeader("user");
        if (userJson == null) {
            return Result.error(401, "请先登录");
        }

        try {
            Map<String, Object> userMap = objectMapper.readValue(userJson, new TypeReference<Map<String, Object>>() {});
            Integer currentUserId = (Integer) userMap.get("id");
            String role = (String) userMap.get("role");

            if (!"admin".equals(role) && !comment.getUserId().equals(currentUserId)) {
                return Result.error(403, "无权限删除此评论");
            }

            commentService.removeById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(500, "身份解析异常：" + e.getMessage());
        }
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public Result<Comment> getById(@PathVariable Integer id) {
        Comment comment = commentService.getById(id);
        return Result.success(comment);
    }
}