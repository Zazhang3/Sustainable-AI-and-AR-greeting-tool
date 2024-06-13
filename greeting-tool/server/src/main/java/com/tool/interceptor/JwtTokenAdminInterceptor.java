package com.tool.interceptor;

import com.tool.constant.JwtClaimsConstant;
import com.tool.context.BaseContext;
import com.tool.properties.JwtProperties;
import com.tool.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor for jwt token verification
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Check JWT
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Check whether the current intercept is a Controller method or another resource
        if (!(handler instanceof HandlerMethod)) {
            //If the current intercept is not a corresponding method, it is released
            return true;
        }

        //Get the token from the request header
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //Check tokens
        try {
            log.info("JWT Verification:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("Current User_id：", userId);
            BaseContext.setCurrentId(userId);
            //Calibration passes, release
            return true;
        } catch (Exception ex) {
            //Checksum failure, response 401 status code
            response.setStatus(401);
            return false;
        }
    }
}
