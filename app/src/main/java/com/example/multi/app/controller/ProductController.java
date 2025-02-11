package com.example.multi.app.controller;


import com.example.multi.category.entity.Category;
import com.example.multi.category.service.CategoryService;
import com.example.multi.app.domain.*;
import com.example.multi.product.entity.Product;
import com.example.multi.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                             @RequestParam(value = "keyword",defaultValue = "" )String keyword) {


        List<Product> products = service.getPage(page, pageSize, keyword);
        List<ProductCellVO> productOnlyVOS = new ArrayList<>();
        for (Product product : products) {
            ProductCellVO productOnlyVO = new ProductCellVO();
            productOnlyVO.setId(product.getId());
            String[] image = product.getImages().split("\\$");
            productOnlyVO.setImage(image[0]);
            productOnlyVO.setInfo(product.getInfo());
            productOnlyVO.setPrice(product.getPrice());
            productOnlyVO.setCategoryName(product.getCategoryName());

            productOnlyVOS.add(productOnlyVO);
        }
        ProductListVO productListVO = new ProductListVO();
        productListVO.setList(productOnlyVOS);
        boolean result = productOnlyVOS.size() < pageSize;
        productListVO.setIsEnd(result);
        return productListVO;
        //return productListVO;
    }

    @RequestMapping("product/info")
    public ProductInfoVO getInfo(@RequestParam(name = "id") BigInteger id){
        Product product = service.getById(id);
        ProductInfoVO productInfoVO = new ProductInfoVO();
        productInfoVO.setTitle(product.getTitle());
        String[]  image = product.getImages().split("\\$");
        List<String> imageList = Arrays.asList(image);
        productInfoVO.setImages(imageList);
        productInfoVO.setName(product.getName());
        productInfoVO.setInfo(product.getInfo());
        productInfoVO.setPrice(product.getPrice());
        productInfoVO.setDetailedTitle(product.getDetailedTitle());
        productInfoVO.setDetailed(product.getDetailed());
        productInfoVO.setCategoryName(product.getCategoryName());
        productInfoVO.setCategoryImage(product.getCategoryImage());


        return productInfoVO;
    }

    @RequestMapping("product/category")
    public ProductCategoryVO getCategory(@RequestParam("page") Integer page,
                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                         @RequestParam(value = "categoryName", defaultValue = "") String categoryName) {
        List<Category> categorys = categoryservice.getCategory(page,pageSize,categoryName);
        List<ProductCateCellVO> productCateCellVOS = new ArrayList<>();
        for (Category category : categorys) {
            ProductCateCellVO productCateCellVO = new ProductCateCellVO();
            productCateCellVO.setCategoryName(category.getName());
            productCateCellVO.setCategoryImage(category.getImage());
            productCateCellVOS.add(productCateCellVO);
        }
        ProductCategoryVO productCategoryVO = new ProductCategoryVO();
        productCategoryVO.setList(productCateCellVOS);
        boolean result = productCateCellVOS.size() < pageSize;
        productCategoryVO.setIsEnd(result);
        return productCategoryVO;

    }


}
