package com.example.multi.module.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigInteger;

@Data
public class User {
    private BigInteger id;
    private String phone;
    private String password;
    private String name;
    private String avatar;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
