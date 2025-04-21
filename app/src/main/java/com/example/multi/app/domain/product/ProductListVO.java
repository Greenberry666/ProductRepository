package com.example.multi.app.domain.product;

import com.example.multi.app.domain.product.ProductCellVO;
import lombok.Data;

import java.util.List;

@Data
public class ProductListVO {
    private List<ProductCellVO> list;
    private Boolean isEnd;
    private String wp;
}
