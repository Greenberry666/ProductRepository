package com.example.multi.console.domain.product;

import com.example.multi.module.utils.RichTextElement;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class ProductConInfoVO {
    private String title;
    private List<String> images;
    private String name;
    private String info;
    private Integer price;
    private String detailedTitle;
    private String detailed;
    private String createTime;
    private String updateTime;
    private BigInteger categoryId;
    private List<RichTextElement> content;
}
