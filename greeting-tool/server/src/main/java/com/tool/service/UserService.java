package com.tool.service;

import com.tool.dto.UserLoginDTO;
import com.tool.entity.User;
import com.tool.result.Result;

@SuppressWarnings("JavadocDeclaration")
public interface UserService {

    /**
     * User Login
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);


}
