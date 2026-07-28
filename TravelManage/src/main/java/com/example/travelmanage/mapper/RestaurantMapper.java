package com.example.travelmanage.mapper;

import com.example.travelmanage.entity.Restaurant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface RestaurantMapper {

    @Select("<script>"
            + "SELECT * FROM restaurant "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if> "
            + "LIMIT #{offset}, #{size}"
            + "</script>")
    List<Restaurant> findByPage(
            @Param("name") String name,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    @Select("<script>"
            + "SELECT COUNT(*) FROM restaurant "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if>"
            + "</script>")
    Long countTotal(@Param("name") String name);

    @Insert("INSERT INTO restaurant(name, type, avg_price, address, region, rating, visit_count, image_url, description) " +
            "VALUES(#{name}, #{type}, #{avgPrice}, #{address}, #{region}, #{rating}, #{visitCount}, #{imageUrl}, #{description})")
    int add(Restaurant restaurant);

    @Update("UPDATE restaurant SET name=#{name}, type=#{type}, avg_price=#{avgPrice}, address=#{address}, region=#{region}, " +
            "rating=#{rating}, visit_count=#{visitCount}, image_url=#{imageUrl}, description=#{description} " +
            "WHERE id=#{id}")
    int update(Restaurant restaurant);

    @Delete("DELETE FROM restaurant WHERE id=#{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT * FROM restaurant WHERE id=#{id}")
    Restaurant findById(@Param("id") Integer id);

    @Select("SELECT * FROM restaurant ORDER BY create_time DESC LIMIT #{limit}")
    List<Restaurant> findLatest(@Param("limit") Integer limit);
}
