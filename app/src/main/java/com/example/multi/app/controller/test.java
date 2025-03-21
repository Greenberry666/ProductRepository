package com.example.multi.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.multi.module.wp.Wp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
public class test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String jsonInput = "{page:1,pageSize:5,keyword:\"零\"}";
        String base64Encoded = Base64.getEncoder().encodeToString(jsonInput.getBytes());

        String decodedWpBase = new String(Base64.getDecoder().decode(base64Encoded),"UTF-8");
        // JSON 解码
        Wp decodedWpJSON = JSON.parseObject(decodedWpBase, Wp.class);
        String keyword ;
        Integer page = decodedWpJSON.getPage();
        Integer pageSize = decodedWpJSON.getPageSize();
        keyword = URLEncoder.encode( "水果","UTF-8");
        System.out.println(keyword);
    }
}
