package com.example.travelmanage.service.impl;

import com.example.travelmanage.entity.ScenicSpot;
import com.example.travelmanage.mapper.ScenicSpotMapper;
import com.example.travelmanage.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScenicSpotServiceImpl implements ScenicSpotService {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    @Override
    public List<ScenicSpot> getScenicList(Integer page, Integer size, String name, String region, String level) {
        int offset = (page - 1) * size;
        return scenicSpotMapper.findByPage(offset, size, name, region, level);
    }

    @Override
    public Integer getTotalCount(String name, String region, String level) {
        return scenicSpotMapper.countAll(name, region, level);
    }

    @Override
    public boolean addScenic(ScenicSpot spot) {
        if (spot.getVisitCount() == null) {
            spot.setVisitCount(0);
        }
        return scenicSpotMapper.addScenicSpot(spot) > 0;
    }

    @Override
    public boolean updateScenic(ScenicSpot spot) {
        return scenicSpotMapper.updateScenicSpot(spot) > 0;
    }

    @Override
    public boolean deleteScenic(Integer id) {
        return scenicSpotMapper.deleteScenicSpot(id) > 0;
    }

    @Override
    public ScenicSpot getScenicById(Integer id) {
        return scenicSpotMapper.findById(id);
    }
}