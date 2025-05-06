package com.example.multi.module.user.service;

import com.example.multi.module.product.entity.Product;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.user.mapper.UserMapper;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.DataUtils;
import com.example.multi.module.utils.SignUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Slf4j
@Service

public class UserService {
    @Resource
    private UserMapper mapper;

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
        return mapper.delete(id, BaseUtils.currentSeconds());
    }

    public BigInteger edit(BigInteger id, String phone, String password, String username, String avatar, String ipAddress) {
        //^ 表示字符串的开始
        //d 表示一个数字字符
        //{11} 表示数字字符必须出现11次
        //$ 表示字符串的结束

        if (!DataUtils.isPhoneNumber(phone)) {
            //phone == null || phone.trim().isEmpty() || !phone.matches("^\\d{11}$")
            throw new IllegalArgumentException("手机号必须为11位数字");
        }
        if (!DataUtils.isPasswordValid(password)) {
            throw new IllegalArgumentException("密码长度必须在6到16位之间");
        }
        if (BaseUtils.isBlank(username)) {
            throw new IllegalArgumentException("产品名称不能为空");
        }
        if (BaseUtils.isBlank(avatar)) {
            throw new IllegalArgumentException("头像不能为空");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        //int timestamp = (int) (System.currentTimeMillis() / 1000);
        int now = BaseUtils.currentSeconds();
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        user.setPassword(encryptedPassword);
        user.setUsername(username);
        user.setAvatar(avatar);
        user.setRegisterIp(ipAddress);
        user.setLastLoginIp(ipAddress);
        user.setLastLoginTime(now);
        user.setCreateTime(BaseUtils.currentSeconds());
        user.setUpdateTime(BaseUtils.currentSeconds());
        user.setIsDeleted(0);
        if (id == null) {
            user.setCreateTime(BaseUtils.currentSeconds());
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

    public User getByPhone(String phone, String countryCode) {
        return mapper.findUserByPhone(phone);
    }

    public User findUserById(BigInteger userId) {
        return mapper.getById(userId);
    }

    public boolean login(String phone, String countryCode, String password,
                         boolean noPasswd, boolean remember, int lifeTime) {
        if (lifeTime < 0) {
            return false;
        }
        //check phone
        if (!DataUtils.isPhoneNumber(phone) || BaseUtils.isEmpty(countryCode)) {
            return false;
        }
        User user = getByPhone(phone, countryCode);
        if (BaseUtils.isEmpty(user)) {
            return false;
        }

        if (noPasswd ||
                SignUtils.marshal(password).equals(user.getPassword())) {
            //write session
            //lifeTime = remember ? lifeTime : 0;
            return true;
        } else {
            return false;
        }
    }

    public boolean login(String phone, String password) {
        return login(phone, "86", password, false, true, SignUtils.getExpirationTime());
    }

    public boolean login(String phone, String password, boolean noPasswd, boolean remember) {
        return login(phone, "86", password, noPasswd, remember, SignUtils.getExpirationTime());
    }

    public void refreshUserLoginContext(BigInteger id, String ip, int time) {
        User user = new User();
        user.setId(id);
        user.setLastLoginIp(ip);
        user.setLastLoginTime(time);
        user.setUpdateTime(time);
        try {
            update(user);
        } catch (Exception exception) {
            log.error("Failed to refresh user login context", exception);

        }
    }

    public User extractByPhone(String phone, String countryCode) {
        return mapper.extractByPhone(phone, countryCode);
    }


}
