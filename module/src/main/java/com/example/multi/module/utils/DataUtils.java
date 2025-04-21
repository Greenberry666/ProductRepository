package com.example.multi.module.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {
    private static final String[] officialPhone = {
            "***"
    };

    //检查输入的字符串是否是一个有效的电话号码
    public static boolean isPhoneNumber(String phone) {
        if (ArrayUtils.contains(officialPhone, phone)) {
            return true;
        }
        String regex = "[1|2]\\d{10}";
        return Pattern.matches(regex, phone);
    }

    //检查输入的字符串是否是一个有效的电子邮件地址
    public static boolean isEmail(String email) {
        final String regex = "[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher mat = pattern.matcher(email);
        return mat.matches();
    }

    //检查输入的密码长度(6-16位)
    public static boolean isPasswordValid(String password) {
        final String regex = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?/`~]{6,16}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher mat = pattern.matcher(password);
        return mat.matches();
    }
}
