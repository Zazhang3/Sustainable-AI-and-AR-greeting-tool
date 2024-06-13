package com.tool.service.impl;

import com.tool.constant.MessageConstant;
import com.tool.dto.UserLoginDTO;
import com.tool.entity.User;
import com.tool.exception.AccountNotFoundException;
import com.tool.exception.PasswordErrorException;
import com.tool.mapper.UserMapper;
import com.tool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@SuppressWarnings("JavadocDeclaration")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * User Login
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {


        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        //Find the corresponding user by username
        User currentUser = userMapper.getByUsername(username);

        //If the user is not found, an exception is thrown
        if(currentUser == null){
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        //MD5 encrypt the entered passwords and compare them
        //Throws an exception if the match fails
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!password.equals(currentUser.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_MISMATCH);
        }

        //Successful login and return the user object
        return currentUser;
    }

}
