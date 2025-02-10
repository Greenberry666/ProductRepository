package com.example.multi.app.category.service;

import com.example.multi.app.category.entity.Category;
import com.example.multi.app.category.mapper.CategoryMapper;
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
public int insert(Category product) {
return mapper.insert(product);
}

// 更新操作
public int update(Category product) {
return mapper.update(product);
}

// 删除操作
public int delete(BigInteger id) {
return mapper.delete((int)(System.currentTimeMillis()/1000),id);
}

public List<Category> getCategory(Integer page,Integer pageSize,String categoryName){
    int offset =(page-1)*pageSize;
    return  mapper.getCategoryName(offset,pageSize,categoryName);}
}