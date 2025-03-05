package com.example.multi.module.product.service;

import com.example.multi.module.category.mapper.CategoryMapper;
import com.example.multi.module.dto.ProductDTO;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.mapper.ProductMapper;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.List;


@Service
public class ProductService {
    @Resource
    private ProductMapper mapper;
    @Resource
    private CategoryMapper categoryMapper;

    public Product getById(BigInteger id) {
        return mapper.getById(id);
    }

    public Product extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    public int insert(Product product) {
        return mapper.insert(product);
    }

    public int update(Product product) {
        return mapper.update(product);
    }

    public BigInteger edit(BigInteger id, String name, String title, String images, String info,
                           Integer price, String detailedTitle, String detailed, BigInteger categoryId) {
        if (BaseUtils.isBlank(title) || title.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("产品标题不能为空,且只能包含汉字/字母、数字和下划线");
        }
        if (BaseUtils.isBlank(name)) {
            throw new IllegalArgumentException("产品名称不能为空");
        }
        if (price < 0 || price > 999999) {
            throw new IllegalArgumentException("产品价格必须在0到999999之间");
        }
        if (BaseUtils.isBlank(images)) {
            throw new IllegalArgumentException("产品样图至少上传一张");
        }
        if (categoryMapper.getById(categoryId) == null) {
            throw new IllegalArgumentException("产品分类 ID 无效，未找到对应的分类");
        }
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        Product product = new Product()
                .setId(id)
                .setName(name)
                .setTitle(title)
                .setImages(images)
                .setInfo(info)
                .setPrice(price)
                .setDetailedTitle(detailedTitle)
                .setDetailed(detailed)
                .setUpdateTime(timestamp)
                .setIsDeleted(0);
        product.setCategoryId(categoryId);
        if (id == null) {
            product.setCreateTime(timestamp);
            insert(product);
            return product.getId();
        } else {
            Product oldProduct = getById(id);
            if (oldProduct == null) {
                throw new RuntimeException("产品更新错误!");
            }
            update(product);
        }
        return id;
    }

    public int delete(BigInteger id) {
        return mapper.delete(id, (int) (System.currentTimeMillis() / 1000));
    }


    //    public List<Product> getPage(Integer page, Integer pageSize, String keyword) {
//        int offset = (page - 1) * pageSize;
//        return mapper.select(offset, pageSize, keyword);
//    }
//    public List<Product> getPage(Integer page, Integer pageSize, String keyword) {
//        List<Integer> categoryIds = mapper.selectIdByCategory(keyword);
//
//        int offset = (page - 1) * pageSize;
//        return mapper.selectProducts(keyword, offset, categoryIds, pageSize);
//
//    }
    public List<Product> getPage(Integer page, Integer pageSize, String keyword) {
        List<BigInteger> categoryIds = mapper.getCategoryIds(keyword);
        StringBuilder idList = new StringBuilder();
        for (int i = 0; i < categoryIds.size(); i++) {
            idList.append(categoryIds.get(i));
            if (i < categoryIds.size() - 1) {
                idList.append(",");
            }
        }
        String ids = idList.toString();

        int offset = (page - 1) * pageSize;
        return mapper.getProducts(keyword,ids,offset, pageSize);
    }



    public int pageCount(String keyword) {
        return mapper.pageCount(keyword);
    }

        public List<ProductDTO> getDTO(Integer page, Integer pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        return mapper.getProductDTO(offset, pageSize, keyword);
    }



}
