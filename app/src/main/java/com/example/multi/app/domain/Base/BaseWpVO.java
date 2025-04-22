package com.example.multi.app.domain.Base;

import lombok.Data;

@Data
public class BaseWpVO {
    private Integer page;
    private Integer pageSize;
    private String keyword;
}
