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

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(User.class) && parameter.hasParameterAnnotation(RequireLogin.class);
    }

    //
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
