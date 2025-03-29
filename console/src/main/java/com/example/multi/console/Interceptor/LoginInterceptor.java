package com.example.multi.console.Interceptor;

import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireLogin requireLogin = handlerMethod.getMethodAnnotation(RequireLogin.class);
            if (requireLogin != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes == null) {
                    throw new IllegalStateException("No thread-bound request found");
                }

                String encodedSign = attributes.getRequest().getHeader("Sign");
                if (encodedSign == null) {
                    attributes.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return false;
                }

                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(encodedSign);
                    String signJson = new String(decodedBytes);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Sign sign = objectMapper.readValue(signJson, Sign.class);
                    int timestamp = (int) (System.currentTimeMillis() / 1000);
                    //sign.getExpireTime()：签名的过期时间戳，表示签名在什么时间点之后不再有效。
                    //currentTimestamp：当前时间的时间戳，表示当前的时间点。

                    if (sign.getExpireTime() < timestamp) {
                        attributes.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sign expired");
                        return false;
                    }

                    User user = userService.findUserById(sign.getUserId());
                    if (user == null) {
                        attributes.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                        return false;
                    }
                } catch (Exception e) {
                    attributes.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Sign");
                    return false;
                }
            }
        }
        return true;
    }
}