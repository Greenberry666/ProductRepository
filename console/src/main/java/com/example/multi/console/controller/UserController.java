package com.example.multi.console.controller;

import com.example.multi.console.domain.ConsoleUserLoginVO;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;

@RestController
public class UserController {
    @Autowired
    private UserService service;

//    @RequestMapping("/user/login")
//    public ConsoleUserLoginVO userLogin(@RequestParam(name = "phone") String phone,
//                                        @RequestParam(name = "password") String password,
//                                        HttpServletResponse response) {
//        ConsoleUserLoginVO userloginVO = new ConsoleUserLoginVO();
//        try {
//            User user = service.findUserByPhone(phone);
//            if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
//                userloginVO.setTips("登录成功");
//                // 生成 Sign
//                int timestamp = (int) (System.currentTimeMillis() / 1000);
//                Sign sign = new Sign();
//                sign.setUserId(user.getId());
//                sign.setExpireTime(timestamp);
//
//                // 将 Sign 转换为 JSON 字符串
//                ObjectMapper objectMapper = new ObjectMapper();
//                String signJson = objectMapper.writeValueAsString(sign);
//
//                // 对 JSON 字符串进行 Base64 编码
//                String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());
//                // 使用 ResponseCookie 创建 Cookie
//                ResponseCookie cookie = ResponseCookie.from("sign", encodedSign)
//                        .path("/") // 设置 Cookie 的路径
//                        //                    分钟 /小时/  天 / 天数的秒数
//                        .maxAge(60 * 60 * 24 * 7) // 设置 Cookie 的有效期（7天）
//                        .httpOnly(true) // 设置 HttpOnly 属性
//                        .secure(true) // 设置 Secure 属性
//                        .build();
//
//                // 将 Cookie 添加到响应中
//                userloginVO.setCookie(cookie.toString());
//
//            } else {
//                userloginVO.setTips("登录失败：账号或密码错误");
//            }
//        } catch (Exception exception) {
//            userloginVO.setTips("登录失败：" + exception.getMessage());
//        }
//        return userloginVO;
//    }
@RequestMapping("/user/login")
public ConsoleUserLoginVO userLogin(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "password") String password,
                                    HttpServletResponse response) {
    ConsoleUserLoginVO userloginVO = new ConsoleUserLoginVO();
    try {
        User user = service.findUserByPhone(phone);
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            userloginVO.setTips("登录成功");
            // 生成 Sign
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            Sign sign = new Sign();
            sign.setUserId(user.getId());
            sign.setExpireTime(timestamp);

            // 将 Sign 转换为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String signJson = objectMapper.writeValueAsString(sign);

            // 对 JSON 字符串进行 Base64 编码
            String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());
            // 使用 ResponseCookie 创建 Cookie
            ResponseCookie cookie = ResponseCookie.from("sign", encodedSign)
                    .path("/") // 设置 Cookie 的路径
                    .maxAge(60 * 60 * 24 * 7) // 设置 Cookie 的有效期（7天）
                    .httpOnly(true) // 设置 HttpOnly 属性
                    .secure(true) // 设置 Secure 属性
                    .build();

            // 直接将 Cookie 添加到响应头
            response.addHeader("Set-Cookie", cookie.toString());
        } else {
            userloginVO.setTips("登录失败：账号或密码错误");
        }
    } catch (Exception exception) {
        userloginVO.setTips("登录失败：" + exception.getMessage());
    }
    return userloginVO;
}
}
