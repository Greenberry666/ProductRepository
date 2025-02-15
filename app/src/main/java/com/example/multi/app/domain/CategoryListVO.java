package com.example.multi.app.domain;

import lombok.Data;

import java.util.List;

@Data
public class CategoryListVO {
    private List<CategoryListCellVO> list;
    private Boolean isEnd;
}
