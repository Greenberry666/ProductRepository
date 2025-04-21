package com.example.multi.console.controller;

import com.alibaba.fastjson.JSON;

import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.console.domain.category.ConsoleUserLoginVO;
import com.example.multi.console.domain.user.UserInfoVO;
import com.example.multi.module.user.service.BaseUserService;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.IpUtils;
import com.example.multi.module.utils.Response;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.utils.SpringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;

@RestController
public class UserController {
    @Autowired
    private BaseUserService service;

    @SneakyThrows
    @RequestMapping("/user/login")
    public Response userLogin(@RequireLogin User loginUser,
                              HttpServletResponse response,
                              @RequestParam(name = "phone") String phone,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "remember") boolean remember) {

        if (!BaseUtils.isEmpty(loginUser)) {
            return new Response(4004, "网络繁忙");
        }

        boolean result;
        if (remember) {
            result = service.login(phone, password);
        } else {

            result = service.login(phone, "86", password, false, false, 0);
        }

        if (!result) {
            return new Response(1010);
        }
        User user = service.getByPhone(phone);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        service.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request), BaseUtils.currentSeconds());


        // 生成 Sign
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        int expireTime = timestamp + (60 * 60 * 24 * 7);
        Sign sign = new Sign();
        sign.setUserId(user.getId());
        sign.setExpireTime(expireTime);

        // 将 Sign 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String signJson = objectMapper.writeValueAsString(sign);

        // 对 JSON 字符串进行 Base64 编码
        String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());

        // 使用 ResponseCookie 创建 Cookie
        Cookie cookie = new Cookie("sign", encodedSign);
        cookie.setPath("/"); // 设置 Cookie 的路径
        cookie.setMaxAge(60 * 60 * 24 * 7); // 设置 Cookie 的有效期（7天）
        cookie.setHttpOnly(true); // 设置 HttpOnly 属性
        // cookie.setSecure(true); // 设置 Secure 属性

        // 直接将 Cookie 添加到响应头
        response.addCookie(cookie);

        // 获取当前请求的 HttpSession
        HttpSession session = request.getSession();
        session.setAttribute(SpringUtils.getProperty("application.session.key"), JSON.toJSONString(user));

        // 构造返回的用户信息
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserName(user.getUsername());
        userInfo.setUserPhone(user.getPhone());
        userInfo.setUserAvatar(user.getAvatar());

        return new Response(1001, userInfo);
    }
}



