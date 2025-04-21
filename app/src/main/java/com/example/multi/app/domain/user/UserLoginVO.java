package com.example.multi.app.domain.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginVO {
    private UserInfoVO userInfo;
    private String sign;

}
