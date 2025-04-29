package com.example.multi.module.productTag.service;

import com.example.multi.module.productTag.entity.ProductTag;
import com.example.multi.module.productTag.mapper.ProductTagMapper;
import com.example.multi.module.tag.entity.Tag;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ProductTagService {

    @Resource
    private ProductTagMapper mapper;


    public List<ProductTag> getById(BigInteger productId) {
        return mapper.getById(productId);
    }

    public Tag extractById(BigInteger productId) {
        return mapper.extractById(productId);
    }

//    public int insert(Tag tag) {
//        return mapper.insert(tag);
//    }
//
//    public int update(Tag tag) {
//        return mapper.update(tag);
//    }
//
//    public int delete(BigInteger id) {
//        return mapper.delete(id, BaseUtils.currentSeconds());
//    }

}
