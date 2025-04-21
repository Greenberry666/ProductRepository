package com.example.multi.console.domain.product;

import com.example.multi.console.domain.product.ProductConCellVO;
import lombok.Data;

import java.util.List;

@Data
public class ProductConListVO {
    private List<ProductConCellVO> list;
    private Integer total;
    private Integer pageSize ;
}
