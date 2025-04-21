package com.example.multi.app.domain.product;

import lombok.Data;

import java.math.BigInteger;
@Data
public class ProductCellVO {
    private BigInteger id;
    private ImageScaleVO image;
    private String info;
    private Integer price;
    private String categoryName;
}
