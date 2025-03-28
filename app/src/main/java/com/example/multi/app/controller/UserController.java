package com.example.multi.app.controller;

import com.example.multi.app.domain.UserLoginVO;
import com.example.multi.app.domain.UserRegisterVO;
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


    @RequestMapping("/user/register")

    public UserRegisterVO userRegister(@RequestParam(name = "phone") String phone,
                                       @RequestParam(name = "password") String password,
                                       @RequestParam(name = "name") String name,
                                       @RequestParam(name = "avatar") String avatar) {
        UserRegisterVO registerVO = new UserRegisterVO();
        try {

            String encryptedPassword = new BCryptPasswordEncoder().encode(password);
            int timestamp = (int) (System.currentTimeMillis() / 1000);

            User user = new User();
            user.setPhone(phone);
            user.setPassword(encryptedPassword);
            user.setName(name);
            user.setAvatar(avatar);
            user.setCreateTime(timestamp);
            user.setUpdateTime(timestamp);
            user.setIsDeleted(0);
            int result = service.insert(user);
            registerVO.setTips(result > 0 ? "注册成功" : "注册失败");
        } catch (Exception exception) {
            registerVO.setTips("注册失败：" + exception.getMessage());
        }
        return registerVO;
    }

    @RequestMapping("/user/login")
    public UserLoginVO userLogin(@RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "password") String password) {
        UserLoginVO userloginVO = new UserLoginVO();
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
