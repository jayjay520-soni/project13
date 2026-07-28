package com.example.travelmanage.service;

import com.example.travelmanage.entity.ScenicSpot;
import java.util.List;

public interface ScenicSpotService {
    List<ScenicSpot> getScenicList(Integer page, Integer size, String name, String region, String level);
    Integer getTotalCount(String name, String region, String level);
    boolean addScenic(ScenicSpot spot);
    boolean updateScenic(ScenicSpot spot);
    boolean deleteScenic(Integer id);
    ScenicSpot getScenicById(Integer id);
}