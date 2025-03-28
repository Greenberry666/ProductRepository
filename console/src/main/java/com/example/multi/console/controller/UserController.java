package com.example.multi.console.controller;

import com.example.multi.console.domain.ConsoleUserLoginVO;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/user/login")
    public ConsoleUserLoginVO userLogin(@RequestParam(name = "phone") String phone,
                                        @RequestParam(name = "password") String password) {
        ConsoleUserLoginVO userloginVO = new ConsoleUserLoginVO();
        try {
            User user = service.findByPhone(phone);
            if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                userloginVO.setTips("登录成功");
                // 生成 Sign
                long expireTime = System.currentTimeMillis() + 3600000;
                Sign sign = new Sign();
                sign.setUserId(user.getId());
                Date date = new Date(expireTime);
                sign.setExpireTime(date);
                // 将 Sign 转换为 JSON 字符串
                ObjectMapper objectMapper = new ObjectMapper();
                String signJson = objectMapper.writeValueAsString(sign);
                // 对 JSON 字符串进行 Base64 编码
                String encodedSign = Base64.getEncoder().encodeToString(signJson.getBytes());
                userloginVO.setSign(encodedSign);
            } else {
                userloginVO.setTips("登录失败：账号或密码错误");
            }
        } catch (Exception exception) {
            userloginVO.setTips("登录失败：" + exception.getMessage());
        }
        return userloginVO;
    }
}
