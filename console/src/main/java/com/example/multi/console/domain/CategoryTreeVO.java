package com.example.multi.console.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class CategoryTreeVO {
    private BigInteger id;
    private String name;
    private String image;
    private List<CategoryTreeVO> children;
}
