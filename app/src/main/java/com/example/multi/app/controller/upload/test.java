package com.example.multi.app.controller.upload;

import com.alibaba.fastjson.JSON;
import com.example.multi.app.domain.Base.BaseWpVO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
public class test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String jsonInput = "{page:1,pageSize:5,keyword:\"零\"}";
        String base64Encoded = Base64.getEncoder().encodeToString(jsonInput.getBytes());

        String decodedWpBase = new String(Base64.getDecoder().decode(base64Encoded),"UTF-8");
        // JSON 解码
        BaseWpVO decodedWpJSON = JSON.parseObject(decodedWpBase, BaseWpVO.class);
        String keyword;
        Integer page = decodedWpJSON.getPage();
        Integer pageSize = decodedWpJSON.getPageSize();
        keyword = URLEncoder.encode( "水果","UTF-8");
        System.out.println(keyword);
    }
}
