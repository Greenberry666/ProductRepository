package com.example.multi.app.controller;

import com.example.multi.app.domain.UserLoginVO;
import com.example.multi.app.domain.UserRegisterVO;
import com.example.multi.module.response.entity.Response;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

//@RestController
//public class UserController {
//    @Autowired
//   private UserService service;
//
//
//    @RequestMapping("/user/register")
//
//    public UserRegisterVO userRegister(@RequestParam(name = "phone") String phone,
//                                       @RequestParam(name = "password") String password,
//                                       @RequestParam(name = "name") String name,
//                                       @RequestParam(name = "avatar") String avatar) {
//       UserRegisterVO registerVO = new UserRegisterVO();
//        try {
//            BigInteger result = service.edit(null, phone, password, name, avatar);
//           registerVO.setTips(result != null ? "注册成功" : "注册失败");
//        } catch (Exception exception) {
//            exception.printStackTrace();
//           registerVO.setTips("注册失败：" + exception.getMessage());
//        }
//        return registerVO;
//    }
//
//
//    @SneakyThrows
//    @RequestMapping("/user/login")
//    public UserLoginVO userLogin(@RequestParam(name = "phone") String phone,
//                                 @RequestParam(name = "password") String password) {
//        UserLoginVO userloginVO = new UserLoginVO();
//
//        User user = service.findUserByPhone(phone);
//        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
//            userloginVO.setTips("登录成功");
//            // 生成 Sign
//            int timestamp = (int) (System.currentTimeMillis() / 1000);
//            int expireTime = timestamp + (60 * 60 * 24 * 7);
//            Sign sign = new Sign();
//            sign.setUserId(user.getId());
//            sign.setExpireTime(expireTime);
//            // 将 Sign 转换为 JSON 字符串
//            ObjectMapper objectMapper = new ObjectMapper();
//            String signJson = objectMapper.writeValueAsString(sign);
//            // 对 JSON 字符串进行 Base64 编码
//           String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());
//           userloginVO.setSign(encodedSign);
//        } else {
//            userloginVO.setTips("登录失败");
//        }
//        return userloginVO;
//    }
//}

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping("/user/register")
    public Response<UserRegisterVO> userRegister(@RequestParam(name = "phone") String phone,
                                                 @RequestParam(name = "password") String password,
                                                 @RequestParam(name = "name") String name,
                                                 @RequestParam(name = "avatar") String avatar) {
        UserRegisterVO registerVO = new UserRegisterVO();
        try {
            BigInteger result = service.edit(null, phone, password, name, avatar);
            if (result != null) {
                registerVO.setTips("注册成功");
                return Response.success(1001, "注册成功", null);
            } else {
                registerVO.setTips("注册失败");
                return Response.error(1003, "注册失败,");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            registerVO.setTips("注册失败：" + exception.getMessage());
            return Response.error(4004, "网络繁忙");
        }
    }

    @SneakyThrows
    @RequestMapping("/user/login")
    public Response<UserLoginVO> userLogin(@RequestParam(name = "phone") String phone,
                                           @RequestParam(name = "password") String password) {
        UserLoginVO userLoginVO = new UserLoginVO();
        User user = service.findUserByPhone(phone);
        if (user == null) {
            userLoginVO.setTips("手机号不存在");
            return Response.error(2001, "手机号不存在");
        }
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            userLoginVO.setTips("登录成功");
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
            userLoginVO.setSign(encodedSign);
            return Response.success(1001, "登录成功", null);
        } else {
            userLoginVO.setTips("登录失败");
            return Response.error(1003, "登录失败");
        }
    }
}

