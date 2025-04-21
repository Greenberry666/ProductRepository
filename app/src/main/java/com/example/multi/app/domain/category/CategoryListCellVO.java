package com.example.multi.app.domain.category;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class CategoryListCellVO {
    private BigInteger categoryId;
    private  String categoryName;
    private String categoryImage;
    private List<CategoryListCellVO> Childrenlist;
}
