package com.tool.service;

import com.tool.dto.UserDTO;
import com.tool.dto.UserEmailDTO;
import com.tool.dto.UserLoginDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;


public interface UserService {

    /**
     * User login
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * User register
     * @param userDTO
     */
    void userRegister(UserDTO userDTO) ;

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


    /**
     * verify Username And Email
     * @param userEmailDTO
     * @return
     */
    User verifyUsernameAndEmail(UserEmailDTO userEmailDTO);

    /**
     * generate Verification Code
     * @param id
     * @return
     */
    String generateVerificationCode(Long id);
}
