package com.tool.mapper;

import com.tool.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * Search for users by username
     *
     */
    @Select("SELECT * FROM user WHERE username=#{username}")
    User getByUsername(String username);

}
