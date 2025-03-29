package com.example.multi.module.sign;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class Sign {
    private BigInteger userId;
    private Integer expireTime;
}
