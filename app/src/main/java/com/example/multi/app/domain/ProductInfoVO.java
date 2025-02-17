package com.example.multi.app.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductInfoVO {
    private String title;
    private Integer createTime;
    private List<String> images;
    private String name;
    private String info;
    private Integer price;
    private String detailedTitle;
    private String detailed;
    private String categoryImage;
    private String categoryName;
}
