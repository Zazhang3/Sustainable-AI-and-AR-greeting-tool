package com.tool.mapper;

import com.tool.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    /**
     * Search for user by username
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
    @Insert("INSERT INTO user (username, password,email) VALUES (#{username},#{password},#{email})")
    void userRegister(User user);

    /**
     * Update user data(update)
     * @param user
     */
    void updateUser(User user);

    /**
     * Delete user by id
     * @param id :user id
     */
    @Delete("DELETE FROM user WHERE id=#{id}")
    void deleteUserById(Long id);

    /**
     * Save verification code and set create time
     * @param id :user id
     * @param verificationCode :randomly generate
     */
    void saveVerificationCode(Long id, String verificationCode, LocalDateTime createTime);

}
