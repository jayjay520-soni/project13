package com.example.travelmanage.mapper;

import com.example.travelmanage.entity.ScenicSpot;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ScenicSpotMapper {

    // 带条件分页查询
    @Select({
            "<script>",
            "SELECT id, name, price, address, region, popularity, level, ",
            "visit_count AS visitCount, image_url AS imageUrl, history ",
            "FROM scenic_spot ",
            "<where>",
            "   <if test=\"name != null and name != ''\">",
            "       AND name LIKE CONCAT('%', #{name}, '%')",
            "   </if>",
            "   <if test=\"region != null and region != ''\">",
            "       AND region = #{region}",
            "   </if>",
            "   <if test=\"level != null and level != ''\">",
            "       AND level = #{level}",
            "   </if>",
            "</where>",
            "LIMIT #{offset}, #{size}",
            "</script>"
    })
    List<ScenicSpot> findByPage(
            @Param("offset") Integer offset,
            @Param("size") Integer size,
            @Param("name") String name,
            @Param("region") String region,
            @Param("level") String level
    );

    // 带条件统计总数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM scenic_spot ",
            "<where>",
            "   <if test=\"name != null and name != ''\">",
            "       AND name LIKE CONCAT('%', #{name}, '%')",
            "   </if>",
            "   <if test=\"region != null and region != ''\">",
            "       AND region = #{region}",
            "   </if>",
            "   <if test=\"level != null and level != ''\">",
            "       AND level = #{level}",
            "   </if>",
            "</where>",
            "</script>"
    })
    Integer countAll(
            @Param("name") String name,
            @Param("region") String region,
            @Param("level") String level
    );

    // 新增景区
    @Insert("INSERT INTO scenic_spot(name,price,address,region,popularity,level,visit_count,image_url,history) " +
            "VALUES(#{name},#{price},#{address},#{region},#{popularity},#{level},#{visitCount},#{imageUrl},#{history})")
    int addScenicSpot(ScenicSpot spot);

    // 更新景区
    @Update("UPDATE scenic_spot SET name=#{name},price=#{price},address=#{address}," +
            "region=#{region},popularity=#{popularity},level=#{level},visit_count=#{visitCount}," +
            "image_url=#{imageUrl},history=#{history} WHERE id=#{id}")
    int updateScenicSpot(ScenicSpot spot);

    // 删除景区
    @Delete("DELETE FROM scenic_spot WHERE id=#{id}")
    int deleteScenicSpot(@Param("id") Integer id);

    // 根据ID查询
    @Select("SELECT id, name, price, address, region, popularity, level, " +
            "visit_count AS visitCount, image_url AS imageUrl, history " +
            "FROM scenic_spot WHERE id=#{id}")
    ScenicSpot findById(@Param("id") Integer id);
}