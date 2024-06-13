package com.tool.service;

import com.tool.dto.UserDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;


public interface UserService {

    /**
     * User login
     * @param userDTO
     * @return
     */
    User login(UserDTO userDTO);

    /**
     * User register
     * @param userDTO
     */
    void addUser(UserDTO userDTO) ;

    /**
     * Update user data
     * @param userUpdateDTO
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * Delete user
     * @param id
     */
    void deleteById(Long id);

}
