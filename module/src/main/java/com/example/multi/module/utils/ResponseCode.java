package com.example.multi.module.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    private static final Map<Integer, String> statusMap = new HashMap<Integer, String>();

    static {
        //登录
        statusMap.put(1001, "OK");
        statusMap.put(1002, "需要登录");
        statusMap.put(1003, "账号被禁用或已删除");
        statusMap.put(1010, "账号密码不匹配或账号不存在");
        //注册'
        statusMap.put(2013, "性别参数不正确");
        statusMap.put(2014, "账号尚未注册");
        statusMap.put(2015, "注册信息未按要求填写");
        statusMap.put(2016, "手机号错误");
        //商品
        statusMap.put(3001, "产品新增失败");
        statusMap.put(3002, "产品更新失败");
        statusMap.put(3051, "产品信息未按要求填写");
        statusMap.put(3052, "产品ID不正确");
        statusMap.put(3053, "产品分类ID不正确");

        //错误
        statusMap.put(4001, "未找到相关用户");
        statusMap.put(4002, "解码 encodedSign出现问题");
        statusMap.put(4003, "没有权限");
        statusMap.put(4004, "网络繁忙");
    }

    public static String getMsg(Integer code) {
        return statusMap.get(code);
    }
}
