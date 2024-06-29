package com.tool.service.impl;

import com.tool.constant.MessageConstant;
import com.tool.dto.UserDTO;
import com.tool.dto.UserEmailDTO;
import com.tool.dto.UserLoginDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;
import com.tool.exception.AccountNotFoundException;
import com.tool.exception.MismatchBetweenEmailAndUsername;
import com.tool.exception.PasswordErrorException;
import com.tool.mapper.UserMapper;
import com.tool.service.UserService;
import com.tool.utils.VerificationCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@SuppressWarnings("JavadocDeclaration")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * User login
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

    /**
     * verify Username And Email
     * @param userEmailDTO
     * @return
     */
    @Override
    public User verifyUsernameAndEmail(UserEmailDTO userEmailDTO) {
        
        User user = userMapper.getByUsername(userEmailDTO.getUsername());
        //Check if user exists and mailbox matches
        if(user == null){
            throw new AccountNotFoundException(MessageConstant.USER_NOT_FOUND);
        } else if (!user.getEmail().equals(userEmailDTO.getEmail())) {
            throw new MismatchBetweenEmailAndUsername(MessageConstant.EMAIL_MISMATCH);
        }

        return user;
    }

    /**
     * generate Verification Code
     * @param id
     * @return
     */
    @Override
    public String generateVerificationCode(Long id) {
        String verificationCode = VerificationCodeGenerator.generateVerificationCode();
        userMapper.saveVerificationCode(id,verificationCode,LocalDateTime.now());

        return verificationCode;
    }


}
