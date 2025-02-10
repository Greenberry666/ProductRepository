package com.example.multi.app.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {
    private List<ProductCateCellVO> list;
    private Boolean isEnd;
}
