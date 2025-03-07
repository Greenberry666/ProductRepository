package com.example.multi.module.category.service;

import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.mapper.CategoryMapper;
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
    public Category getById(BigInteger id) {
        return mapper.getById(id);
    }

    // 根据ID提取操作
    public Category extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    // 插入操作
    public int insert(Category category) {
        return mapper.insert(category);
    }

    // 更新操作
    public int update(Category category) {
        return mapper.update(category);
    }

    // 删除操作
    public int delete(BigInteger id) {
        return mapper.delete((int) (System.currentTimeMillis() / 1000), id);
    }



    public BigInteger edit(BigInteger id, String name, String image) {
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
            insert(category);

            return category.getId();
        } else {
            Category oldcategory = mapper.getById(id);
            if (oldcategory == null) {
                throw new RuntimeException("产品分类更新错误!");
            }
            update(category);
        }
        return id;
    }

    public List<Category> getParentCategorys() {
        return mapper.getParentCategorys();
    }

    public List<Category> getChildrenCategorys() {
        return mapper.getChildrenCategorys();
    }

    public List<Category> getByIds(String tagIds) {
        return mapper.getIds(tagIds);
    }
    public List<BigInteger> getAllCategory(){
        return mapper.getAllUsedCategory();
    }







}

