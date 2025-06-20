package com.example.multi.module.textMessage.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TextMessage {
    private BigInteger id;
    private String phone;
    private String code;
    private Integer sendTime;
    private String status;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
