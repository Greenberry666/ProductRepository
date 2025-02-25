package com.example.multi.app.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CategoryListCellVO {
    private BigInteger categoryId;
    private  String categoryName;
    private  String categoryImage;
}
