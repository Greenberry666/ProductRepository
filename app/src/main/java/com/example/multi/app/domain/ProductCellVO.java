package com.example.multi.app.domain;

import lombok.Data;

import java.math.BigInteger;
@Data
public class ProductCellVO {
    private BigInteger id;
    private String image;
    private String info;
    private Integer price;
    private String categoryName;
}
