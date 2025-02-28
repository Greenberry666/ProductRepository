package com.example.multi.app.controller;

import com.alibaba.fastjson.JSON;
import java.util.Base64;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.app.domain.*;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.wp.Wp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryservice;

    @RequestMapping("/product/list")
    public ProductListVO productAll(@RequestParam("page") Integer page,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {


        List<Product> products = service.getPage(page, pageSize, keyword);
        List<ProductCellVO> productCellVOS = new ArrayList<>();
        for (Product product : products) {
            Category category = categoryservice.getById(product.getCategoryId());
            if (category == null) {
                continue;
            }
            ProductCellVO productCellVO = new ProductCellVO();
            productCellVO.setId(product.getId());
            String[] image = product.getImages().split("\\$");
            productCellVO.setImage(image[0]);
            productCellVO.setInfo(product.getInfo());
            productCellVO.setPrice(product.getPrice());
            productCellVO.setCategoryName(category.getName());
            productCellVOS.add(productCellVO);

        }
        ProductListVO productListVO = new ProductListVO();
        productListVO.setList(productCellVOS);
        boolean result = productCellVOS.size() < pageSize;
        productListVO.setIsEnd(result);
        return productListVO;
    }

    @SneakyThrows
    @RequestMapping("/product/testlist")
    public ProductListVO testproductAll(@RequestParam(value = "wp",defaultValue = "") String wp,
                                        @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Integer page = 1;
        Integer pageSize = 5;

        if (wp != null && !wp.isEmpty() ) {
            // Base64 解码
            String decodedWpBase = new String(Base64.getDecoder().decode(wp),"UTF-8");
            // JSON 解码
             Wp decodedWpJSON = JSON.parseObject(decodedWpBase, Wp.class);
             page = decodedWpJSON.getPage();
             pageSize = decodedWpJSON.getPageSize();}

        List<Product> products = service.getPage(page, pageSize, keyword);

        List<ProductCellVO> productCellVOS = new ArrayList<>();

        for (Product product : products) {
            Category category = categoryservice.getById(product.getCategoryId());
            if (category == null) {
                continue;
            }
            ProductCellVO productCellVO = new ProductCellVO();
            productCellVO.setId(product.getId());
            String[] image = product.getImages().split("\\$");
            productCellVO.setImage(image[0]);
            productCellVO.setInfo(product.getInfo());
            productCellVO.setPrice(product.getPrice());
            productCellVO.setCategoryName(category.getName());
            productCellVOS.add(productCellVO);

        }
        ProductListVO productListVO = new ProductListVO();
        productListVO.setIsEnd(productCellVOS.size() < pageSize);
        productListVO.setList(productCellVOS);
        return productListVO;

    }


    @RequestMapping("product/info")
    public ProductInfoVO getInfo(@RequestParam(name = "id") BigInteger id) {
        ProductInfoVO productInfoVO = new ProductInfoVO();

        Product product = service.getById(id);
        if (product == null) {
            productInfoVO.setTitle("未找到对应的产品信息");
            return productInfoVO;
        }
        Category category = categoryservice.getById(product.getCategoryId());
        if (category == null) {
            productInfoVO.setCategoryName("未找到对应的分类信息");
            productInfoVO.setCategoryImage("未找到对应的分类信息");
        } else {
            productInfoVO.setCategoryName(category.getName());
            productInfoVO.setCategoryImage(category.getImage());
        }
        productInfoVO.setTitle(product.getTitle());
        String[] image = product.getImages().split("\\$");
        List<String> imageList = Arrays.asList(image);
        productInfoVO.setCreateTime(product.getCreateTime());
        productInfoVO.setImages(imageList);
        productInfoVO.setName(product.getName());
        productInfoVO.setInfo(product.getInfo());
        productInfoVO.setPrice(product.getPrice());
        productInfoVO.setDetailedTitle(product.getDetailedTitle());
        productInfoVO.setDetailed(product.getDetailed());
        return productInfoVO;
    }


}
