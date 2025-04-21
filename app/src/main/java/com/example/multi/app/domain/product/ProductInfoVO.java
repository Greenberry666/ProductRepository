package com.example.multi.app.domain.product;

import com.example.multi.module.utils.RichTextElement;
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
    //private String detailed;
    private List<RichTextElement> content;
    //private List<String> detailed;
    private String categoryImage;
    private String categoryName;
}
