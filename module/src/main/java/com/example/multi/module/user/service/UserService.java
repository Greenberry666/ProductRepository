package com.example.multi.module.user.service;

import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserService {
    @Resource
    private UserMapper mapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public int insert(User user) {
//        String encryptedPassword = passwordEncoder.encode(password);
//        int timestamp = (int) (System.currentTimeMillis() / 1000);
//        User user = new User();
//        user.setId(id);
//        user.setPhone(phone);
//        user.setPassword(encryptedPassword);
//        user.setName(name);
//        user.setAvatar(avatar);
//        user.setCreateTime(timestamp);
//        user.setUpdateTime(timestamp);
//        user.setIsDeleted(0);
        return mapper.insert(user);
    }

    public User findByPhone(String phone) {
        return mapper.findByPhone(phone);
    }

    public User findById(BigInteger userId) {
        return mapper.findById(userId);
    }


}
