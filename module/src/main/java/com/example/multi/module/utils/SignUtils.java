package com.example.multi.module.utils;

import com.alibaba.fastjson.JSON;
import com.example.multi.module.user.entity.UserSign;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SignUtils {
    private final static String SIGN_SALT = "_2019_dream";
    //14天过期
    private final static int EXPIRATION_TIME = 1209600;
    private static final String PASSWORD_SALT = "__MLZ_RED";

    private SignUtils() {

    }

    //获取签名的有效期（单位：秒）
    public static int getExpirationTime() {
        return EXPIRATION_TIME;
    }

    //生成用户签名
    public static String makeSign(BigInteger userId) {
        UserSign sign = new UserSign();
        sign.setExpiration(BaseUtils.currentSeconds() + EXPIRATION_TIME);
        sign.setSalt(SIGN_SALT);
        sign.setUserId(userId);
        byte[] rawSign = Base64.getEncoder().encode(JSON.toJSONString(sign).getBytes(StandardCharsets.UTF_8));
        return new String(rawSign, StandardCharsets.UTF_8).trim();
    }

    //解析用户签名，验证其有效性和过期时间
    public static BigInteger parseSign(String sign) {
        if (BaseUtils.isEmpty(sign)) {
            return null;
        }

        byte[] bytes = Base64.getDecoder().decode(sign.getBytes(StandardCharsets.UTF_8));
        String rawSign = new String(bytes, StandardCharsets.UTF_8);
        UserSign userSign = null;
        try {
            userSign = JSON.parseObject(rawSign, UserSign.class);
        } catch (Exception cause) {
            // ignores
        }
        if (userSign == null) {
            return null;
        }

        int time = userSign.getExpiration();
        int current = BaseUtils.currentSeconds();
        if (current > time) {
            // a expired sign
            return null;
        }

        return userSign.getUserId();
    }

    //对密码进行加密
    public static String marshal(String password) {
        return BaseUtils.md5(PASSWORD_SALT + password);
    }
}
