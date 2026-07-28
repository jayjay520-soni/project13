package com.example.travelmanage.mapper;

import com.example.travelmanage.entity.Homestay;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface HomestayMapper {

    @Select("<script>"
            + "SELECT * FROM homestay "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if> "
            + "LIMIT #{offset}, #{size}"
            + "</script>")
    List<Homestay> findByPage(
            @Param("name") String name,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    @Select("<script>"
            + "SELECT COUNT(*) FROM homestay "
            + "<if test='name != null and name != \"\"'>WHERE name LIKE CONCAT('%', #{name}, '%')</if>"
            + "</script>")
    Long countTotal(@Param("name") String name);

    @Insert("INSERT INTO homestay(name, type, price, address, region, rating, room_count, image_url, description) " +
            "VALUES(#{name}, #{type}, #{price}, #{address}, #{region}, #{rating}, #{roomCount}, #{imageUrl}, #{description})")
    int add(Homestay homestay);

    @Update("UPDATE homestay SET name=#{name}, type=#{type}, price=#{price}, address=#{address}, region=#{region}, " +
            "rating=#{rating}, room_count=#{roomCount}, image_url=#{imageUrl}, description=#{description} " +
            "WHERE id=#{id}")
    int update(Homestay homestay);

    @Delete("DELETE FROM homestay WHERE id=#{id}")
    int delete(@Param("id") Integer id);

    @Select("SELECT * FROM homestay WHERE id=#{id}")
    Homestay findById(@Param("id") Integer id);

    @Select("SELECT * FROM homestay ORDER BY create_time DESC LIMIT #{limit}")
    List<Homestay> findLatest(@Param("limit") Integer limit);
}
