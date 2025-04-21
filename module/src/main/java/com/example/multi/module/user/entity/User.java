package com.example.multi.module.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.multi.module.utils.BaseUtils;
import lombok.Data;

import java.math.BigInteger;

@Data
public class User {
    private BigInteger id;

    private String countryCode;
    private String phone;
    private String email;

    private String password;
    private String username;
    private String avatar;

//    private String personalProfile;
//    private String coverImage;

//    private Integer gender;
//    private String birthday;

//    private String wechatOpenId;
//    private String wechatUnionId;
//    private String wechatNo;
//    private String country;

//    private String province;
//    private String city;

    //    private Integer registerTime;
    private String registerIp;

    private Integer lastLoginTime;
    private String lastLoginIp;

    private Integer isBan;

//    private String extra;

    private Integer createTime;
    private Integer updateTime = BaseUtils.currentSeconds();
    private Integer isDeleted;


}
