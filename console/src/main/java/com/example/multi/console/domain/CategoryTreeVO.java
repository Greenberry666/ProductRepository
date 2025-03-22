package com.example.multi.console.domain;


import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeVO {
    private String name;
    private List<CategoryTreeVO> children;
}
