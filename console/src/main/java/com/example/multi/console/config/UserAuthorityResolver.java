package com.example.multi.console.config;


import com.alibaba.fastjson.JSON;
import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.BaseUserService;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.SignUtils;
import com.example.multi.module.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.ApplicationArguments;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigInteger;

@Slf4j
public class UserAuthorityResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private BaseUserService userService;

    //isCheckAuthority：一个布尔值，用于控制是否检查用户权限。这个值通过 ApplicationArguments 动态配置
    private boolean isCheckAuthority;

    public UserAuthorityResolver(ApplicationArguments appArguments) {
        String[] arguments = appArguments.getSourceArgs();
        if (arguments == null || arguments.length <= 3) {
            isCheckAuthority = true;
            return;
        }

        String isMockUserLogin = arguments[2];
        if (BaseUtils.isEmpty(isMockUserLogin)) {
            isCheckAuthority = true;
        } else {
            isCheckAuthority = Boolean.parseBoolean(isMockUserLogin);
        }
        //log.info("Check user authority: {}", Boolean.toString(isCheckAuthority));
    }


    //判断当前解析器是否支持解析某个方法参数。
    //检查方法参数的类型是否为 User
    //检查方法参数是否被 @RequireLogin 注解标记
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(User.class) && parameter.hasParameterAnnotation(RequireLogin.class);
    }

    //解析方法参数中的用户信息。
    //检查是否需要验证权限：如果 isCheckAuthority 为 false，直接返回默认用户（ID 为 1 的用户）
    //获取请求类型：从配置中获取 application.isapp，判断当前请求是否为 App 请求。
    //App 请求：
    //   从请求头中获取 Sign。
    //   使用 SignUtils.parseSign 解析 Sign，获取用户 ID。
    //   如果用户 ID 不为空，通过 userService.getById 查询用户信息并返回。
    //Web 请求：
    //   从会话中获取 application.session.key 对应的值。
    //   如果值不为空，使用 JSON.parseObject 将其解析为 User 对象并返回。
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest request,
                                  WebDataBinderFactory factory) {

        if (isCheckAuthority) {
            String isAppS = SpringUtils.getProperty("application.isapp");
            boolean isApp = isAppS.equals("1") ? true : false;
            jakarta.servlet.http.HttpServletRequest sRequest = (HttpServletRequest) request.getNativeRequest();
            //HttpServletRequest sRequest = request.getNativeRequest(HttpServletRequest.class);
            if (isApp) {
                String signKey = SpringUtils.getProperty("application.sign.key");
                String sign = sRequest.getHeader(signKey);
                if (!BaseUtils.isEmpty(sign)) {
                    BigInteger userId = SignUtils.parseSign(sign);
                    log.info("userId: {}, sign: {}", userId, sign);
                    if (!BaseUtils.isEmpty(userId)) {
                        return userService.getById(userId);
                    }
                }
                return null;
            } else {
                HttpSession session = sRequest.getSession(false);
                if (BaseUtils.isEmpty(session)) {
                    return null;
                }
                String signKey = SpringUtils.getProperty("application.session.key");
                Object value = session.getAttribute(signKey);
                if (value == null) {
                    return null;
                }

                String sValue = (String) value;
                return JSON.parseObject(sValue, User.class);
            }
        }

        return userService.getById(BigInteger.valueOf(1));
    }
}
