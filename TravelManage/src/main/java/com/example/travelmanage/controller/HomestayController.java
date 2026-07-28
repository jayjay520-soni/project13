package com.example.travelmanage.controller;

import com.example.travelmanage.entity.Homestay;
import com.example.travelmanage.mapper.HomestayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/homestay")
@CrossOrigin
public class HomestayController {

    @Autowired
    private HomestayMapper homestayMapper;

    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name
    ) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * size;
        List<Homestay> list = homestayMapper.findByPage(name, offset, size);
        Long total = homestayMapper.countTotal(name);
        result.put("code", 1);
        result.put("data", list);
        result.put("total", total);
        return result;
    }

    @GetMapping("/all")
    public Map<String, Object> all() {
        Map<String, Object> result = new HashMap<>();
        List<Homestay> list = homestayMapper.findByPage(null, 0, 1000);
        result.put("code", 1);
        result.put("data", list);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Homestay homestay) {
        Map<String, Object> result = new HashMap<>();
        int rows = homestayMapper.add(homestay);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "添加成功" : "添加失败");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Homestay homestay) {
        Map<String, Object> result = new HashMap<>();
        int rows = homestayMapper.update(homestay);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int rows = homestayMapper.delete(id);
        result.put("code", rows > 0 ? 1 : 0);
        result.put("msg", rows > 0 ? "删除成功" : "删除失败");
        return result;
    }
}
