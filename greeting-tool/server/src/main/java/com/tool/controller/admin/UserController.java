package com.tool.controller.admin;

import com.tool.constant.JwtClaimsConstant;
import com.tool.dto.UserDTO;
import com.tool.dto.UserUpdateDTO;
import com.tool.entity.User;
import com.tool.properties.JwtProperties;
import com.tool.result.Result;
import com.tool.service.UserService;
import com.tool.utils.JwtUtil;
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
@RequestMapping("/admin/user")
@Api(tags = "User-related Interface")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * login
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("User login")
    public Result<UserLoginVO> login(@RequestBody UserDTO userDTO) {
        log.info("Login User: {}", userDTO);

        //Login
        User user = userService.login(userDTO);

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
     * Logout
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("User logout")
    public Result<String> logout() {
        log.info("Logout User");
        return Result.success();
    }

    /**
     * User register
     * @param userDTO
     */
    @PostMapping("/register")
    @ApiOperation("User register")
    public Result userRegister(@RequestBody UserDTO userDTO) {
        log.info("User register: {}", userDTO);
        userService.userRegister(userDTO);
        return Result.success();
    }

    /**
     * Update user data
     * @param userUpdateDTO
     * @return
     */
    @PutMapping
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
}
