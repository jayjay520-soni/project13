package com.example.travelmanage.mapper;

import com.example.travelmanage.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    // 根据用户名查询（登录用）
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    // 用户注册（自动设置创建时间、角色、状态）
    // UserMapper.java
    @Insert("INSERT INTO user(username,password,phone,role,create_time,status) VALUES(#{username},#{password},#{phone},'user',NOW(),1)")
    int addUser(User user);


    // 管理员查询用户列表：只查普通用户 + 用户名模糊搜索 + 状态筛选
    @Select("<script>"
            + "SELECT id, username, role, status, "
            + "create_time AS createTime, "
            + "last_login_time AS lastLoginTime "
            + "FROM user WHERE role = 'user' "
            + "<if test='username != null and username != \"\"'>AND username LIKE CONCAT('%', #{username}, '%')</if> "
            + "<if test='status != null'>AND status = #{status}</if> "
            + "</script>")
    List<User> findAll(@Param("username") String username,
                       @Param("status") Integer status);

    // 更新用户启用/禁用状态
    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id,
                     @Param("status") Integer status);

    // 更新最近登录时间
    @Update("UPDATE user SET last_login_time = NOW() WHERE id = #{id}")
    int updateLastLoginTime(@Param("id") Integer id);
}
