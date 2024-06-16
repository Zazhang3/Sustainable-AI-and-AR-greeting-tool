package com.tool.service.impl;

import com.tool.constant.MessageConstant;
import com.tool.dto.UserDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;
import com.tool.exception.AccountNotFoundException;
import com.tool.exception.PasswordErrorException;
import com.tool.mapper.UserMapper;
import com.tool.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@SuppressWarnings("JavadocDeclaration")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * User login
     * @param userDTO
     * @return
     */
    @Override
    public User login(UserDTO userDTO) {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

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

    /**
     * User register
     * @param userDTO
     */
    @Override
    public void userRegister(UserDTO userDTO) {
        User newUser = new User();

        //MD5 encrypts passwords
        userDTO.setPassword(DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));

        //Copy Properties
        BeanUtils.copyProperties(userDTO, newUser);

        userMapper.userRegister(newUser);
    }

    /**
     * Update user data
     * @param userUpdateDTO
     */
    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {

        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO,user);
        String password = DigestUtils.md5DigestAsHex(userUpdateDTO.getPassword().getBytes());
        user.setPassword(password);
        userMapper.updateUser(user);

    }

    /**
     * Delete user
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        User user = userMapper.getById(id);
        if(user == null){
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        }
        userMapper.deleteUserById(id);
    }


}
