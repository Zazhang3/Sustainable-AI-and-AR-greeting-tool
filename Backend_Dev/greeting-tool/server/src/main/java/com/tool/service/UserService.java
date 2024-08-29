package com.tool.service;

import com.tool.dto.UserDTO;
import com.tool.dto.UserEmailDTO;
import com.tool.dto.UserLoginDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;


public interface UserService {

    /**
     * User login
     * @param userLoginDTO user login data
     * @return user
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * User register
     * @param userDTO user data
     */
    void userRegister(UserDTO userDTO) ;

    /**
     * Update user data
     * @param userUpdateDTO user update data
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * Delete user
     * @param id user id
     */
    void deleteById(Long id);


    /**
     * verify Username And Email
     * @param userEmailDTO user data with email
     * @return user
     */
    User verifyUsernameAndEmail(UserEmailDTO userEmailDTO);

    /**
     * generate Verification Code
     * @param id user id
     * @return verification code
     */
    String generateVerificationCode(Long id);
}
