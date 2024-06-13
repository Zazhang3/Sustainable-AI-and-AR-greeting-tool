package com.tool.controller.admin;

import com.tool.constant.JwtClaimsConstant;
import com.tool.dto.UserLoginDTO;
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
     * @param userLoginDTO
     * @return
     */
    @PostMapping
    @ApiOperation("User Login")
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



}
