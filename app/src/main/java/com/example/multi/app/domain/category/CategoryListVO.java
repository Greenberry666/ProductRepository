package com.example.multi.app.domain.category;

import com.example.multi.app.domain.category.CategoryListCellVO;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class CategoryListVO {
    private List<CategoryListCellVO> Childrenlist;
    private BigInteger categoryId;
    private  String categoryName;
    private  String categoryImage;

}
