package com.example.multi.app.controller.user;

import com.example.multi.app.annotation.RequireLogin;
import com.example.multi.app.domain.user.UserInfoVO;
import com.example.multi.app.domain.user.UserLoginVO;
import com.example.multi.app.domain.user.UserRegisterVO;
import com.example.multi.module.user.service.BaseUserService;
import com.example.multi.module.user.service.UserDefine;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.IpUtils;
import com.example.multi.module.utils.Response;
import com.example.multi.module.sign.Sign;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.service.UserService;
import com.example.multi.module.utils.SignUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigInteger;
import java.util.Base64;


@RestController
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping("/user/register")
    public Response userRegister(@RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "username") String username,
                                 @RequestParam(name = "avatar") String avatar) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        // 检查用户是否已经注册
        User user = service.extractByPhone(phone, "86");

        if (!BaseUtils.isEmpty(user)) {
            // 如果用户已存在且被禁止登录
            if (user.getIsDeleted().equals(1) || user.getIsBan().equals(1)) {
                return new Response(1002);
            }
            // 刷新用户登录上下文
            service.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request), BaseUtils.currentSeconds());
            return new Response(1001);
        }

        // 注册新用户
        try {
            BigInteger newUserId = service.edit(null, phone, password, username, avatar, IpUtils.getIpAddress(request));
            if (newUserId != null) {
                return new Response(1001);
            } else {
                return new Response(4004);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Response(2015, exception.getMessage());
        }
    }

    @SneakyThrows
    @RequireLogin
//    @RequestMapping("/user/login")
//    public Response userLogin(
//                              @RequestParam(name = "phone") String phone,
//                              @RequestParam(name = "password") String password) {
////        if (!BaseUtils.isEmpty(loginUser)) {
////            return new Response(4004, "网络繁忙");
////        }
//
//        UserLoginVO userLoginVO = new UserLoginVO();
//        User user = service.getByPhone(phone, "86");
//
//        if (user == null) {
//            return new Response(2016);
//        }
//        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
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
//            String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());
//            userLoginVO.setSign(encodedSign);
//
//            UserInfoVO userInfo = new UserInfoVO();
//            userInfo.setName(user.getUsername());
//            userInfo.setPhone(user.getPhone());
//            userInfo.setAvatar(user.getAvatar());
//            userInfo.setUserId(user.getId());
//
//            //UserLoginVO loginInfo = new UserLoginVO();
//            //userLoginVO.setSign(SignUtils.makeSign(user.getId()));
//            userLoginVO.setUserInfo(userInfo);
//            jakarta.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
//            service.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request), BaseUtils.currentSeconds());
//
//            return new Response(1001, userLoginVO);
//        } else {
//            return new Response(1010);
//        }
    @RequestMapping("/user/login")
    public Response userLogin(@RequestParam(name = "phone") String phone,
                              @RequestParam(name = "password") String password) {
        User user = service.getByPhone(phone, "86");

        if (user == null) {
            return new Response(2016);
        }
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            int timestamp = (int) (System.currentTimeMillis() / 1000);
            int expireTime = timestamp + (60 * 60 * 24 * 7);
            Sign sign = new Sign();
            sign.setUserId(user.getId());
            sign.setExpireTime(expireTime);

            ObjectMapper objectMapper = new ObjectMapper();
            String signJson = objectMapper.writeValueAsString(sign);
            String encodedSign = Base64.getUrlEncoder().encodeToString(signJson.getBytes());

            UserInfoVO userInfo = new UserInfoVO();
            userInfo.setName(user.getUsername());
            userInfo.setPhone(user.getPhone());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setUserId(user.getId());

            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setSign(encodedSign);
            userLoginVO.setUserInfo(userInfo);

            jakarta.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            service.refreshUserLoginContext(user.getId(), IpUtils.getIpAddress(request), BaseUtils.currentSeconds());

            return new Response(1001, userLoginVO);
        } else {
            return new Response(1010);
        }
    }

}

