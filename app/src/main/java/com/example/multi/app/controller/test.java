package com.example.multi.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.multi.module.wp.Wp;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String jsonInput = "{ page: 4,pageSize: 5}";
        String base64Encoded = Base64.getEncoder().encodeToString(jsonInput.getBytes());

        String decodedWpBase = new String(Base64.getDecoder().decode(base64Encoded),"UTF-8");
        // JSON 解码
        Wp decodedWpJSON = JSON.parseObject(decodedWpBase, Wp.class);


        Integer page = decodedWpJSON.getPage();
        Integer pageSize = decodedWpJSON.getPageSize();
        System.out.println(base64Encoded);
    }
}
