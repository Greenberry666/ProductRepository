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

    public List<BigInteger> getTagIdsByProductId(BigInteger productId) {
        return mapper.findTagIdsByProductId(productId);
    }

    public List<ProductTag> getProductTagsToExcel(){return mapper.getProductTagToExcel();}


}
