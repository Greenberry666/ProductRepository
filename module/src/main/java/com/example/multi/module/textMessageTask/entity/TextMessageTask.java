package com.example.multi.module.textMessageTask.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TextMessageTask {
    private BigInteger id;
    private String phone;
    private String code;
    private Integer status;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
