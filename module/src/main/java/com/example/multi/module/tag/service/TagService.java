package com.example.multi.module.tag.service;

import com.example.multi.module.productTag.entity.ProductTag;
import com.example.multi.module.productTag.mapper.ProductTagMapper;
import com.example.multi.module.tag.entity.Tag;
import com.example.multi.module.tag.mapper.TagMapper;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Resource
    private TagMapper mapper;

    @Resource
    private ProductTagMapper productTagMapper;

    public Tag getById(String name) {
        return mapper.getByName(name);
    }

    public Tag extractById(String name) {
        return mapper.extractByName(name);
    }

    public int insert(Tag tag) {
        return mapper.insert(tag);
    }

    public int update(Tag tag) {
        return mapper.update(tag);
    }

    public int delete(BigInteger id) {
        return mapper.delete(id, BaseUtils.currentSeconds());
    }

    public List<BigInteger> editToTags(String tags) {
        List<BigInteger> tagIds = new ArrayList<>();
        String[] tagNames = tags.split(",");

        for (String tagName : tagNames) {
            // 去除多余空格
            tagName = tagName.trim();
            if (!tagName.isEmpty()) {
                // 查询数据库查标签是否存在
                Tag existingTag = mapper.getByName(tagName);

                if (existingTag == null) {

                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag.setCreateTime(BaseUtils.currentSeconds());
                    newTag.setUpdateTime(BaseUtils.currentSeconds());
                    newTag.setIsDeleted(0);
                    insert(newTag);
                    tagIds.add(newTag.getId());
                } else {
                    // 如果标签已存在直接用存在的标签
                    tagIds.add(existingTag.getId());
                }
            }
        }
        return tagIds;
    }

    public void manageTags(BigInteger productId, List<BigInteger> tagIds) {
        // 1. 获取当前商品的所有标签关系
        List<ProductTag> existingProductTags = productTagMapper.getById(productId);

        // 2. 删除不再需要的标签关系
        List<ProductTag> toDelete = new ArrayList<>();
        for (ProductTag pt : existingProductTags) {
            boolean found = false;
            for (BigInteger tagId : tagIds) {
                if (pt.getTagId().equals(tagId)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                toDelete.add(pt);
            }
        }

        for (ProductTag pt : toDelete) {
            productTagMapper.delete(pt.getId(), BaseUtils.currentSeconds());
        }

        // 3. 添加新的标签关系
        for (BigInteger tagId : tagIds) {
            boolean found = false;
            for (ProductTag pt : existingProductTags) {
                if (pt.getTagId().equals(tagId)) {
                    found = true;
                    break;
                }
            }

//            if (!found) {
//                ProductTag productTag = new ProductTag();
//                productTag.setProductId(productId);
//                productTag.setTagId(tagId);
//                productTag.setCreateTime(BaseUtils.currentSeconds());
//                productTag.setUpdateTime(BaseUtils.currentSeconds());
//                productTag.setIsDeleted(0);
//                productTagMapper.insert(productTag);
//            }

            if (!found) {

                ProductTag existingTag = productTagMapper.getDeletedProductTag(productId, tagId);
                if (existingTag != null) {
                    // 恢复记录
                    existingTag.setIsDeleted(0);
                    existingTag.setUpdateTime(BaseUtils.currentSeconds());

                    productTagMapper.update(existingTag);
                } else {
                    // 插入新记录
                    ProductTag newTag = new ProductTag();
                    newTag.setProductId(productId);
                    newTag.setTagId(tagId);
                    newTag.setCreateTime(BaseUtils.currentSeconds());
                    newTag.setUpdateTime(BaseUtils.currentSeconds());
                    newTag.setIsDeleted(0);
                    productTagMapper.insert(newTag);
                }
            }
        }
    }

    //    public List<String> findTagNamesByIds(List<BigInteger> tagIds){
//        return mapper.getTagNamesByIds(tagIds);
//    }
    public List<String> findTagNamesByIds(List<BigInteger> tagIds) {
        return mapper.getTagNamesByIds(tagIds);
    }


}
