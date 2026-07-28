package com.example.travelmanage.controller;

import com.example.travelmanage.entity.ScenicSpot;
import com.example.travelmanage.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/scenic")
@CrossOrigin
public class ScenicSpotController {

    @Autowired
    private ScenicSpotService scenicSpotService;

    @GetMapping("/list")
    public Map<String, Object> getScenicList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String level
    ) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<ScenicSpot> list = scenicSpotService.getScenicList(page, size, name, region, level);
            Integer total = scenicSpotService.getTotalCount(name, region, level);

            map.put("code", 1);
            map.put("msg", "查询成功");
            map.put("data", list);
            map.put("total", total);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "查询失败：" + e.getMessage());
        }
        return map;
    }

    @PostMapping("/add")
    public Map<String, Object> addScenic(@RequestBody ScenicSpot spot) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean ok = scenicSpotService.addScenic(spot);
            if (ok) {
                map.put("code", 1);
                map.put("msg", "新增成功");
            } else {
                map.put("code", 0);
                map.put("msg", "新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "新增异常：" + e.getMessage());
        }
        return map;
    }

    @PostMapping("/update")
    public Map<String, Object> updateScenic(@RequestBody ScenicSpot spot) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean ok = scenicSpotService.updateScenic(spot);
            if (ok) {
                map.put("code", 1);
                map.put("msg", "修改成功");
            } else {
                map.put("code", 0);
                map.put("msg", "修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "修改异常：" + e.getMessage());
        }
        return map;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteScenic(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean ok = scenicSpotService.deleteScenic(id);
            if (ok) {
                map.put("code", 1);
                map.put("msg", "删除成功");
            } else {
                map.put("code", 0);
                map.put("msg", "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "删除异常：" + e.getMessage());
        }
        return map;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        ScenicSpot spot = scenicSpotService.getScenicById(id);
        if (spot != null) {
            map.put("code", 1);
            map.put("msg", "查询成功");
            map.put("data", spot);
        } else {
            map.put("code", 0);
            map.put("msg", "数据不存在");
        }
        return map;
    }
}