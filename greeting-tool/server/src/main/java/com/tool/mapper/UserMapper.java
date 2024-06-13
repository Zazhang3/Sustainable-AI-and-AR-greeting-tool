package com.tool.mapper;

import com.tool.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * Search for users by username
     * @param username
     */
    @Select("SELECT * FROM user WHERE username=#{username}")
    User getByUsername(String username);

    /**
     * Search for users by id
     * @param id
     */
    @Select("SELECT * FROM user WHERE id=#{id}")
    User getById(Long id);

    /**
     * Add new user(insert)
     * @param user
     */
    @Insert("INSERT INTO user (username, password) VALUES (#{username},#{password})")
    void addUser(User user);

    /**
     * Update user data(update)
     * @param user
     */
    void updateUser(User user);

    /**
     * Delete user by id
     * @param id
     */
    @Delete("DELETE FROM user WHERE id=#{id}")
    void deleteUserById(Long id);

}
