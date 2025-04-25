package com.example.multi.app.Interceptor;

import com.example.multi.app.annotation.RequireLogin;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import com.example.multi.module.utils.Response;
import com.example.multi.module.utils.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Base64;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //HandlerMethod：表示当前请求对应的方法，通过它获取方法的注解信息
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //如果方法被 @RequireLogin 注解标记，则需要进行登录验证。
            RequireLogin requireLogin = handlerMethod.getMethodAnnotation(RequireLogin.class);
            if (requireLogin != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes == null) {
                    sendErrorResponse(response, 4003);
                    return false;
                }

                String encodedSign = attributes.getRequest().getHeader("sign");
                if (encodedSign == null || encodedSign.trim().isEmpty()) {
                    sendErrorResponse(response, 4003);
                    return false;
                }


                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(encodedSign);
                    String signJson = new String(decodedBytes);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Sign sign = objectMapper.readValue(signJson, Sign.class);
                    int timestamp = (int) (System.currentTimeMillis() / 1000);

                    if (sign.getExpireTime() < timestamp) {
                        sendErrorResponse(response, 4001);
                        return false;
                    }

                    User user = userService.findUserById(sign.getUserId());
                    if (user == null) {
                        sendErrorResponse(response, 4002);
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    sendErrorResponse(response, 4002);
                    return false;
                } catch (JsonProcessingException e) {
                    sendErrorResponse(response, 4001);
                    return false;
                } catch (Exception e) {
                    sendErrorResponse(response, 4001);
                    return false;
                }
            }
        }
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        Response<String> responseObj = new Response<>(statusCode);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseObj));
    }
}
