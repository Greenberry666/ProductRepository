package com.example.multi.module.product.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("图片")
    private String images;

    @ExcelProperty("信息")
    private String info;

    @ExcelProperty("价格")
    private Integer price;

    @ExcelProperty("详细标题")
    @TableField("detailed_title")
    private String detailedTitle;

    @ExcelProperty("详细信息")
    private String detailed;

    @ExcelProperty("重量")
    private Integer weight;

    @ExcelProperty("创建时间")
    @TableField("create_time")
    private Integer createTime;

    @ExcelProperty("更新时间")
    @TableField("update_time")
    private Integer updateTime;

    @ExcelProperty("是否删除")
    @TableField("is_deleted")
    private Integer isDeleted;

    @ExcelProperty("分类ID")
    private BigInteger categoryId;
}