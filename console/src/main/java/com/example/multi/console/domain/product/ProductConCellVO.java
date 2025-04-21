package com.example.multi.console.domain.product;

import lombok.Data;

import java.math.BigInteger;


@Data
public class ProductConCellVO {
    private BigInteger id;
    private String image;
    private String info;
    private Integer price;
    private String title;

}
