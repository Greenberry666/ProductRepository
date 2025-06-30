package com.example.multi.module.product.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
//    @ExcelProperty("序号")
    private BigInteger id;

    @ExcelProperty("序号")
    private String idStr;

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
    private String detailedTitle;

    @ExcelProperty("详细信息")
    private String detailed;

    @ExcelProperty("重量")
    private Integer weight;

    @ExcelProperty("创建时间")
    private Integer createTime;

    @ExcelProperty("更新时间")
    private Integer updateTime;

    @ExcelProperty("是否删除")
    private Integer isDeleted;

    @ExcelProperty("分类ID")
    private BigInteger categoryId;

}