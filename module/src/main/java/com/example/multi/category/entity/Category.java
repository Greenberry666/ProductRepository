package com.example.multi.category.entity;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Category {
    //
    private Long id;
    //
    private String name;
    //
    private String image;
    //
    private Integer createTime;
    //
    private Integer updateTime;
    //
    private Byte isDeleted;
}