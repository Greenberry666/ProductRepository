package com.example.multi.module.productTag.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ProductTag {
    private BigInteger id;

    private BigInteger productId;

    private BigInteger tagId;

    private Integer createTime;

    private Integer updateTime;

    private Integer isDeleted;

}
