package com.tool.controller;

import com.tool.constant.JwtClaimsConstant;
import com.tool.dto.UserDTO;
import com.tool.dto.UserEmailDTO;
import com.tool.dto.UserLoginDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;
import com.tool.properties.JwtProperties;
import com.tool.result.Result;
import com.tool.service.UserService;
import com.tool.utils.JwtUtil;
import com.tool.vo.UserEmailVO;
import com.tool.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "User-related Interface")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * login
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("User login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("Login User: {}", userLoginDTO);

        //Login
        User user = userService.login(userLoginDTO);

        //Generate JWT token after successful login
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //Inject data into VO object
        UserLoginVO userLoginVO =UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token).build();

        return Result.success(userLoginVO);
    }

    /**
     * User register
     * @param userDTO
     */
    @PostMapping("/register")
    @ApiOperation("User register")
    public Result<UserLoginVO> userRegister(@RequestBody UserDTO userDTO) {
        log.info("User register: {}", userDTO);
        UserLoginDTO userLoginDTOWithUnencryptedPassword = new UserLoginDTO();
        userLoginDTOWithUnencryptedPassword.setUsername(userDTO.getUsername());
        userLoginDTOWithUnencryptedPassword.setPassword(userDTO.getPassword());
        userService.userRegister(userDTO);
        return login(userLoginDTOWithUnencryptedPassword);
    }

    /**
     * Update user data
     * @param userUpdateDTO
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("Update user data")
    public Result updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("Update User: {}", userUpdateDTO);
        userService.updateUser(userUpdateDTO);
        return Result.success();
    }

    /**
     * Delete user
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("delete user")
    public Result<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * get user eamil by userId & generate verification code
     * @param userEmailDTO
     * @return :userEmailVO
     */
    @PostMapping("/verification")
    @ApiOperation("check email address and username")
    public Result<UserEmailVO> userEmailVerification(@RequestBody UserEmailDTO userEmailDTO) {
        log.info("check userByUserName: {}", userEmailDTO);

        User user = userService.verifyUsernameAndEmail(userEmailDTO);
        String verificationCode = userService.generateVerificationCode(user.getId());

        UserEmailVO userEmailVO = new UserEmailVO();
        userEmailVO.setVerificationCode(verificationCode);
        userEmailVO.setUsername(user.getUsername());

        return Result.success(userEmailVO);
    }

}
