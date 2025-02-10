package com.example.multi.app.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductListVO {
    private List<ProductCellVO> list;
    private Boolean isEnd;
}
