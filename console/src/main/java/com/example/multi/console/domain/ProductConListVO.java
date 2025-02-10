package com.example.multi.console.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProductConListVO {
    private List<ProductConCellVO> list;
    private Integer total;
    private Integer pageSize ;
}
