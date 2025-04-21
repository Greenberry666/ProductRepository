package com.example.multi.app.domain.user;

import lombok.Data;

import java.math.BigInteger;

@Data
public class UserInfoVO {
    private BigInteger userId;
    private String name;
    private Integer gender;
    private String phone;
    private String avatar;
}
