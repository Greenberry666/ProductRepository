package com.example.multi.console.domain.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVO {
    private String userName;
    private String userPhone;
    private String userAvatar;
}
