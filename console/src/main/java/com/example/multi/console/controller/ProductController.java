package com.example.multi.console.controller;

import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.console.domain.*;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.response.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//@RestController
//public class ProductController {
//    @Autowired
//    private ProductService service;
//    @Autowired
//    private CategoryService categoryservice;
//
//    @RequireLogin
//    @RequestMapping("/product/list")
//    public ProductConListVO page(@RequestParam("page") Integer page,
//                                 @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
//                                 @RequestParam(value = "keyword",defaultValue = "" ) String keyword){
//
//
//        List<Product> products = service.getPage(page   ,pageSize,keyword);
//
//        List<ProductConCellVO>  productConOnlyVOS = new ArrayList<>();
//        for(Product product : products){
//
//            ProductConCellVO productConOnlyVO = new ProductConCellVO();
//            productConOnlyVO.setId(product.getId());
//            productConOnlyVO.setTitle(product.getTitle());
//            String[]  image = product.getImages().split("\\$");
//            productConOnlyVO.setImage(image[0]);
//            productConOnlyVO.setInfo(product.getInfo());
//            productConOnlyVO.setPrice(product.getPrice());
//            productConOnlyVOS.add(productConOnlyVO);
//        }
//        ProductConListVO productConListVO = new ProductConListVO();
//        productConListVO.setTotal(service.pageCount(keyword));
//        productConListVO.setPageSize(pageSize);
//        productConListVO.setList(productConOnlyVOS);
//        return productConListVO;
//    }
//
//
//    @RequestMapping("/product/info")
//    public ProductConInfoVO getInfo(@RequestParam(name = "id") BigInteger id) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ProductConInfoVO productConInfoVO = new ProductConInfoVO();
//
//
//                Product product = service.getById(id);
//        if (product == null) {
//            productConInfoVO.setTitle("未找到对应的产品信息");
//            return productConInfoVO;
//        }
//        Category category = categoryservice.getById(product.getCategoryId());
//        if (category == null) {
//            productConInfoVO.setTitle("未找到对应的分类信息");
//            return productConInfoVO;
//        }
//                productConInfoVO.setTitle(product.getTitle());
//                String[] image = product.getImages().split("\\$");
//                List<String> imageList = Arrays.asList(image);
//                productConInfoVO.setImages(imageList);
//                productConInfoVO.setName(product.getName());
//                productConInfoVO.setInfo(product.getInfo());
//                productConInfoVO.setPrice(product.getPrice());
//                productConInfoVO.setDetailedTitle(product.getDetailedTitle());
//                productConInfoVO.setDetailed(product.getDetailed());
//                productConInfoVO.setCreateTime(dateFormat.format(product.getCreateTime() * 1000l));
//                productConInfoVO.setUpdateTime(dateFormat.format(product.getUpdateTime() * 1000l));
//                productConInfoVO.setCategoryId(product.getCategoryId());
//        return productConInfoVO;
//    }
//
//    @RequestMapping("/product/create")
//    public ConsoleCreateVO productCreate(@RequestParam(name = "name") String name,
//                                                         @RequestParam(name = "title") String title,
//                                                         @RequestParam(name = "images") String images,
//                                                         @RequestParam(name = "info") String info,
//                                                         @RequestParam(name = "price") Integer price,
//                                                         @RequestParam(name = "detailedTitle") String detailedTitle,
//                                                         @RequestParam(name = "detailed") String detailed,
//                                                         @RequestParam(name = "categoryId")BigInteger categoryId) {
//        ConsoleCreateVO consoleCreateVO = new ConsoleCreateVO();
//        try {
//            BigInteger result = service.edit(null, name, title, images, info, price, detailedTitle, detailed,categoryId);
//            consoleCreateVO.setTips(result != null ? "成功" : "失败");
//            consoleCreateVO.setId(result);
//        } catch (Exception exception) {
//
//            consoleCreateVO.setTips(exception.getMessage());
//        }
//        return consoleCreateVO;
//    }
//
//    @RequestMapping("/product/update")
//    public ConsoleUpdateVO Update(@RequestParam(name = "id") BigInteger id,
//                                  @RequestParam(name = "name") String name,
//                                  @RequestParam(name = "title") String title,
//                                  @RequestParam(name = "images") String images,
//                                  @RequestParam(name = "info") String info,
//                                  @RequestParam(name = "price") Integer price,
//                                  @RequestParam(name = "detailedTitle") String detailedTitle,
//                                  @RequestParam(name = "detailed") String detailed,
//                                  @RequestParam(name = "categoryId") BigInteger categoryId){
//        ConsoleUpdateVO consoleUpdateVO = new ConsoleUpdateVO();
//        try {
//            BigInteger result = service.edit(id, name, title, images, info, price, detailedTitle, detailed,categoryId);
//            consoleUpdateVO.setTips(result != null ? "成功" : "失败");
//            consoleUpdateVO.setId(id);
//        } catch (Exception exception) {
//            consoleUpdateVO.setTips(exception.getMessage());
//        }
//        return consoleUpdateVO;
//    }
//
//    @RequestMapping("/product/delete")
//    public ConsoleDeleteVO Deleted(@RequestParam(name = "id") BigInteger id){
//        int result = service.delete(id);
//        ConsoleDeleteVO consoleDeleteVO = new ConsoleDeleteVO();
//        consoleDeleteVO.setTips(result  == 1  ? "成功" : "失败");
//        return  consoleDeleteVO;
//    }

@RestController
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryService;

    @RequireLogin
    @RequestMapping("/product/list")
    public Response<ProductConListVO> page(@RequestParam("page") Integer page,
                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                           @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Product> products = service.getPage(page, pageSize, keyword);

        List<ProductConCellVO> productConOnlyVOS = new ArrayList<>();
        for (Product product : products) {
            ProductConCellVO productConOnlyVO = new ProductConCellVO();
            productConOnlyVO.setId(product.getId());
            productConOnlyVO.setTitle(product.getTitle());
            String[] image = product.getImages().split("\\$");
            productConOnlyVO.setImage(image[0]);
            productConOnlyVO.setInfo(product.getInfo());
            productConOnlyVO.setPrice(product.getPrice());
            productConOnlyVOS.add(productConOnlyVO);
        }

        ProductConListVO productConListVO = new ProductConListVO();
        productConListVO.setTotal(service.pageCount(keyword));
        productConListVO.setPageSize(pageSize);
        productConListVO.setList(productConOnlyVOS);

        return Response.success(productConListVO);
    }

    @RequestMapping("/product/info")
    public Response<ProductConInfoVO> getInfo(@RequestParam(name = "id") BigInteger id) {
        Product product = service.getById(id);
        if (product == null) {
            return Response.error(2001, "未找到对应的产品信息");
        }

        Category category = categoryService.getById(product.getCategoryId());
        if (category == null) {
            return Response.error(2002, "未找到对应的分类信息");
        }

        ProductConInfoVO productConInfoVO = new ProductConInfoVO();
        productConInfoVO.setTitle(product.getTitle());
        String[] image = product.getImages().split("\\$");
        productConInfoVO.setImages(Arrays.asList(image));
        productConInfoVO.setName(product.getName());
        productConInfoVO.setInfo(product.getInfo());
        productConInfoVO.setPrice(product.getPrice());
        productConInfoVO.setDetailedTitle(product.getDetailedTitle());
        productConInfoVO.setDetailed(product.getDetailed());
        productConInfoVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getCreateTime()));
        productConInfoVO.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getUpdateTime()));
        productConInfoVO.setCategoryId(product.getCategoryId());

        return Response.success(productConInfoVO);
    }

    @RequestMapping("/product/create")
    public Response<ConsoleCreateVO> productCreate(@RequestParam(name = "name") String name,
                                                   @RequestParam(name = "title") String title,
                                                   @RequestParam(name = "images") String images,
                                                   @RequestParam(name = "info") String info,
                                                   @RequestParam(name = "price") Integer price,
                                                   @RequestParam(name = "detailedTitle") String detailedTitle,
                                                   @RequestParam(name = "detailed") String detailed,
                                                   @RequestParam(name = "categoryId") BigInteger categoryId) {
        ConsoleCreateVO consoleCreateVO = new ConsoleCreateVO();
        try {
            BigInteger result = service.edit(null, name, title, images, info, price, detailedTitle, detailed, categoryId);
            if (result != null) {
                consoleCreateVO.setId(result);
                return Response.success(consoleCreateVO);
            } else {
                return Response.error(4004, "网络繁忙");
            }
        } catch (Exception exception) {
            return Response.error(3001, "产品新增失败:" + exception.getMessage());
        }
    }

    @RequestMapping("/product/update")
    public Response<ConsoleUpdateVO> update(@RequestParam(name = "id") BigInteger id,
                                            @RequestParam(name = "name") String name,
                                            @RequestParam(name = "title") String title,
                                            @RequestParam(name = "images") String images,
                                            @RequestParam(name = "info") String info,
                                            @RequestParam(name = "price") Integer price,
                                            @RequestParam(name = "detailedTitle") String detailedTitle,
                                            @RequestParam(name = "detailed") String detailed,
                                            @RequestParam(name = "categoryId") BigInteger categoryId) {
        ConsoleUpdateVO consoleUpdateVO = new ConsoleUpdateVO();
        try {
            BigInteger result = service.edit(id, name, title, images, info, price, detailedTitle, detailed, categoryId);
            if (result != null) {
                consoleUpdateVO.setId(id);
                return Response.success(consoleUpdateVO);
            } else {
                return Response.error(4004, "网络繁忙");
            }
        } catch (Exception exception) {
            return Response.error(3002, "产品更新失败:" + exception.getMessage());
        }
    }

    @RequestMapping("/product/delete")
    public Response<ConsoleDeleteVO> deleted(@RequestParam(name = "id") BigInteger id) {
        int result = service.delete(id);
        if (result == 1) {
            return Response.success(new ConsoleDeleteVO());
        } else {
            return Response.error(3003, "产品删除失败");
        }
    }






}
