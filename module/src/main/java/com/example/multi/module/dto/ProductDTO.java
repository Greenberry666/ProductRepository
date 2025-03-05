package com.example.multi.module.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ProductDTO {
    private BigInteger id;
    private String images;
    private String info;
    private Integer price;
    private String categoryName;

}
