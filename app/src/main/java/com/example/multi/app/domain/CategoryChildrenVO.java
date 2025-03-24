package com.example.multi.app.domain;

import com.example.multi.module.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class CategoryChildrenVO {
    private BigInteger categoryId;
    private String categoryName;
    private String categoryImage;
    private List<CategoryListCellVO> Childrenlist;
    private List<Product> productList;

}
