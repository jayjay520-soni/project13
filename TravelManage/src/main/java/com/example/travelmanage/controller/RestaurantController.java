package com.example.travelmanage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travelmanage.common.Result;
import com.example.travelmanage.entity.Restaurant;
import com.example.travelmanage.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // 分页 + 模糊搜索 + 筛选
    @GetMapping("/list")
    public Result<Page<Restaurant>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String type
    ) {
        Page<Restaurant> pageInfo = new Page<>(page, size);

        QueryWrapper<Restaurant> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        if (region != null && !region.isEmpty()) {
            wrapper.eq("region", region);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        wrapper.orderByDesc("id");

        Page<Restaurant> data = restaurantService.page(pageInfo, wrapper);
        return Result.success(data);
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody Restaurant restaurant) {
        boolean success = restaurantService.save(restaurant);
        return success ? Result.success("新增成功") : Result.error(500, "新增失败");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Restaurant restaurant) {
        boolean success = restaurantService.updateById(restaurant);
        return success ? Result.success("更新成功") : Result.error(500, "更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        boolean success = restaurantService.removeById(id);
        return success ? Result.success("删除成功") : Result.error(500, "删除失败");
    }

    @GetMapping("/{id}")
    public Result<Restaurant> getById(@PathVariable Integer id) {
        Restaurant restaurant = restaurantService.getById(id);
        return Result.success(restaurant);
    }
}