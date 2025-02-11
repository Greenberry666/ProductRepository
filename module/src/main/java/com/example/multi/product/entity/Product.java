package com.example.multi.product.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Accessors(chain = true)
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private BigInteger id;
    private String title;
    private String name;
    private String images;
    private String info;
    private Integer price;
    @TableField("detailed_title")
    private String detailedTitle;
    private String detailed;
    private Integer weight;
    @TableField("create_time")
    private Integer createTime;
    @TableField("update_time")
    private Integer updateTime;
    @TableField("is_deleted")
    private Integer isDeleted;
    private String categoryName;
    private String categoryImage;

}

