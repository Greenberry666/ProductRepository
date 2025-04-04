package com.example.multi.module.user.service;

import com.example.multi.module.product.entity.Product;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.mapper.UserMapper;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserService {
    @Resource
    private UserMapper mapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public User getById(BigInteger id) {
        return mapper.getById(id);
    }

    public User extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    public int insert(User user) {
        return mapper.insert(user);
    }

    public int update(User user) {
        return mapper.update(user);
    }

    public int delete(BigInteger id) {
        return mapper.delete((int) (System.currentTimeMillis() / 1000), id);
    }
//    public int insert(User user) {
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
//        return mapper.insert(user);
//    }

    public BigInteger edit(BigInteger id, String phone, String password, String name, String avatar) {
        //^ 表示字符串的开始
        //d 表示一个数字字符
        //{11} 表示数字字符必须出现11次
        //$ 表示字符串的结束
        if (phone == null || phone.trim().isEmpty() || !phone.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("手机号必须为11位数字");
        }
        if (password.length() < 6 || password.length() > 16) {
            throw new IllegalArgumentException("密码长度必须在6到16位之间");
        }
        if (BaseUtils.isBlank(name)) {
            throw new IllegalArgumentException("产品名称不能为空");
        }
        if (BaseUtils.isBlank(avatar)) {
            throw new IllegalArgumentException("头像不能为空");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        user.setPassword(encryptedPassword);
        user.setName(name);
        user.setAvatar(avatar);
        user.setCreateTime(timestamp);
        user.setUpdateTime(timestamp);
        user.setIsDeleted(0);
        if (id == null) {
            user.setCreateTime(timestamp);
            insert(user);
            return user.getId();
        } else {
            User oldUser = getById(id);
            if (oldUser == null) {
                throw new RuntimeException("用户更新错误!");
            }
            update(user);
        }
        return id;
    }

    public User findUserByPhone(String phone) {
        return mapper.findUserByPhone(phone);
    }

    public User findUserById(BigInteger userId) {
        return mapper.getById(userId);
    }


}
