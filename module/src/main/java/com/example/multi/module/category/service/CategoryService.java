package com.example.multi.module.category.service;

import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.mapper.CategoryMapper;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.List;

@Service
public class CategoryService {

@Resource
private CategoryMapper mapper;

// 根据ID查询操作
public Category getById(BigInteger id) {return mapper.getById(id);}

// 根据ID提取操作
public Category extractById(BigInteger id) {
return mapper.extractById(id);
}

// 插入操作
public BigInteger insert(BigInteger id,String name,String image) {
    if (BaseUtils.isBlank(name)) {
        throw new IllegalArgumentException("产品名称不能为空");
    }
    if (BaseUtils.isBlank(image)) {
        throw new IllegalArgumentException("产品样图至少上传一张");
    }
    Category category = new Category();
    category.setId(id);
    category.setName(name);
    category.setImage(image);
    category.setCreateTime((int)(System.currentTimeMillis()/1000));
    category.setUpdateTime((int)(System.currentTimeMillis()/1000));
    category.setIsDeleted(0);

 mapper.insert(category);
 return category.getId();
}

// 更新操作
public BigInteger update(BigInteger id,String name,String image) {
    if (BaseUtils.isBlank(name)) {
        throw new IllegalArgumentException("产品名称不能为空");
    }
    if (BaseUtils.isBlank(image)) {
        throw new IllegalArgumentException("产品样图至少上传一张");
    }
    Category category = new Category();
    category.setId(id);
    category.setName(name);
    category.setImage(image);
    category.setCreateTime((int)(System.currentTimeMillis()/1000));
    category.setUpdateTime((int)(System.currentTimeMillis()/1000));
    category.setIsDeleted(0);

    mapper.update(category);
    return category.getId();

}

// 删除操作
public int delete(BigInteger id) {
return mapper.delete((int)(System.currentTimeMillis()/1000),id);
}

public List<Category> getCategory(Integer page,Integer pageSize,String categoryName){
    int offset =(page-1)*pageSize;
    return  mapper.getCategoryName(offset,pageSize,categoryName);}

    public BigInteger edit(BigInteger id, String name,String image) {

        if (BaseUtils.isBlank(name)) {
            throw new IllegalArgumentException("产品名称不能为空");
        }
        if (BaseUtils.isBlank(image)) {
            throw new IllegalArgumentException("产品样图至少上传一张");
        }
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        Category category = new Category()
                .setId(id)
                .setName(name)
                .setImage(image)
                .setUpdateTime(timestamp)
                .setIsDeleted(0);
        if (id == null) {
            category.setCreateTime(timestamp);
            mapper.insert(category);
            return category.getId();
        } else {
            Category oldcategory = mapper.getById(id);
            if (oldcategory == null) {
                throw new RuntimeException("产品分类更新错误!");
            }
            mapper.update(category);
        }
        return id;
    }
}

