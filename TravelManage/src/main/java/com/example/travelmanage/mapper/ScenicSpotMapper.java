package com.example.travelmanage.mapper;

import com.example.travelmanage.entity.ScenicSpot;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface ScenicSpotMapper {

    // 分页查询（支持按名称搜索）
    @Select("<script>"
            + "SELECT * FROM scenic_spot "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if> "
            + "LIMIT #{offset}, #{size}"
            + "</script>")
    List<ScenicSpot> findByPage(
            @Param("name") String name,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    // 统计总数（用于分页）
    @Select("<script>"
            + "SELECT COUNT(*) FROM scenic_spot "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if>"
            + "</script>")
    Long countTotal(@Param("name") String name);

    // 新增景点
    @Insert("INSERT INTO scenic_spot(name, price, address, region, popularity, level, visit_count, image_url, history) " +
            "VALUES(#{name}, #{price}, #{address}, #{region}, #{popularity}, #{level}, #{visitCount}, #{imageUrl}, #{history})")
    int add(ScenicSpot spot);

    // 更新景点
    @Update("UPDATE scenic_spot SET name=#{name}, price=#{price}, address=#{address}, region=#{region}, " +
            "popularity=#{popularity}, level=#{level}, visit_count=#{visitCount}, image_url=#{imageUrl}, history=#{history} " +
            "WHERE id=#{id}")
    int update(ScenicSpot spot);

    // 删除景点
    @Delete("DELETE FROM scenic_spot WHERE id=#{id}")
    int delete(@Param("id") Integer id);

    // 根据ID查询
    @Select("SELECT * FROM scenic_spot WHERE id=#{id}")
    ScenicSpot findById(@Param("id") Integer id);
}
