package com.example.multi.module.tag.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Tag {
    private BigInteger id;
    //
    private String name;
    //
    private Integer createTime;
    //
    private Integer updateTime;
    //
    private Integer isDeleted;


}
