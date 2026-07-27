package com.example.travelmanage.controller;

import com.example.travelmanage.entity.ScenicSpot;
import com.example.travelmanage.mapper.ScenicSpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scenic")
@CrossOrigin  // 解决跨域问题
public class ScenicSpotController {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    // 分页查询（带搜索）
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * size;
        List<ScenicSpot> list = scenicSpotMapper.findByPage(name, offset, size);
        Long total = scenicSpotMapper.countTotal(name);
        result.put("code", 1);
        result.put("data", list);
        result.put("total", total);
        return result;
    }

    // 新增景点
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ScenicSpot spot) {
        Map<String, Object> result = new HashMap<>();
        int rows = scenicSpotMapper.add(spot);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "添加成功" : "添加失败");
        return result;
    }

    // 更新景点
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody ScenicSpot spot) {
        Map<String, Object> result = new HashMap<>();
        int rows = scenicSpotMapper.update(spot);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "更新成功" : "更新失败");
        return result;
    }

    // 删除景点
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int rows = scenicSpotMapper.delete(id);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "删除成功" : "删除失败");
        return result;
    }
}
