package com.example.multi.module.category.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain = true)
public class Category {
    //
    private BigInteger id;
    //
    private String name;
    //
    private String image;
    //
    private Integer createTime;
    //
    private Integer updateTime;
    //
    private Integer isDeleted;
}