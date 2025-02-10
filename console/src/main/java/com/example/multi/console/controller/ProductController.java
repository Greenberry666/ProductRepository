package com.example.multi.console.controller;

import com.example.multi.console.domain.*;
import com.example.multi.module.entity.Product;
import com.example.multi.module.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class ProductController {
    @Autowired
    private ProductService service;


    @RequestMapping("/product/list")
    public ProductConListVO page(@RequestParam("page") Integer page,
                                 @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                 @RequestParam(value = "keyword",defaultValue = "" ) String keyword){


            List<Product> products = service.getPage(page   ,pageSize,keyword);

            List<ProductConCellVO>  productConOnlyVOS = new ArrayList<>();
            for(Product product : products){

                ProductConCellVO productConOnlyVO = new ProductConCellVO();
                productConOnlyVO.setId(product.getId());
                productConOnlyVO.setTitle(product.getTitle());
                String[]  image = product.getImages().split("\\$");
                productConOnlyVO.setImage(image[0]);
                productConOnlyVO.setInfo(product.getInfo());
                productConOnlyVO.setPrice(product.getPrice());
                productConOnlyVOS.add(productConOnlyVO);
            }
            ProductConListVO productConListVO = new ProductConListVO();
            productConListVO.setTotal(service.pageCount(keyword));
            productConListVO.setPageSize(pageSize);
            productConListVO.setList(productConOnlyVOS);
            return productConListVO;
    }


    @RequestMapping("/product/info")
    public ProductConInfoVO getInfo(@RequestParam(name = "id") BigInteger id){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Product product = service.getById(id);
        ProductConInfoVO productConInfoVO = new ProductConInfoVO();
        productConInfoVO .setTitle(product.getTitle());
        String[]  image = product.getImages().split("\\$");
        List<String> imageList = Arrays.asList(image);
        productConInfoVO .setImages(imageList);
        productConInfoVO .setName(product.getName());
        productConInfoVO .setInfo(product.getInfo());
        productConInfoVO .setPrice(product.getPrice());
        productConInfoVO .setDetailedTitle(product.getDetailedTitle());
        productConInfoVO .setDetailed(product.getDetailed());
        productConInfoVO .setCreateTime(dateFormat.format(product.getCreateTime()* 1000l));
        productConInfoVO .setUpdateTime(dateFormat.format(product.getUpdateTime()* 1000l));


        return productConInfoVO ;
    }

    @RequestMapping("/product/infoid")
    public ProductConInfoVO getInfoId(@RequestParam(name = "id") BigInteger id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Product product = service.extractById(id);
        ProductConInfoVO productConInfoVO = new ProductConInfoVO();
        productConInfoVO.setTitle(product.getTitle());
        String[] image = product.getImages().split("\\$");
        List<String> imageList = Arrays.asList(image);
        productConInfoVO.setImages(imageList);
        productConInfoVO.setName(product.getName());
        productConInfoVO.setInfo(product.getInfo());
        productConInfoVO.setPrice(product.getPrice());
        productConInfoVO.setDetailedTitle(product.getDetailedTitle());
        productConInfoVO.setDetailed(product.getDetailed());
        productConInfoVO.setCreateTime(dateFormat.format(product.getCreateTime() * 1000l));
        productConInfoVO.setUpdateTime(dateFormat.format(product.getUpdateTime() * 1000l));


        return productConInfoVO;
    }

    @RequestMapping("/product/create")
    public ConsoleCreateVO productCreate(@RequestParam(name = "name") String name,
                                                         @RequestParam(name = "title") String title,
                                                         @RequestParam(name = "images") String images,
                                                         @RequestParam(name = "info") String info,
                                                         @RequestParam(name = "price") Integer price,
                                                         @RequestParam(name = "detailedTitle") String detailedTitle,
                                                         @RequestParam(name = "detailed") String detailed) {
        ConsoleCreateVO consoleCreateVO = new ConsoleCreateVO();
        try {
            BigInteger result = service.edit(null, name, title, images, info, price, detailedTitle, detailed);
            consoleCreateVO.setTips(result != null ? "成功" : "失败");
            consoleCreateVO.setId(result);
        } catch (Exception exception) {
            consoleCreateVO.setTips(exception.getMessage());
        }
        return consoleCreateVO;
    }

    @RequestMapping("/product/update")
    public ConsoleUpdateVO Update(@RequestParam(name = "id") BigInteger id,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "title") String title,
                                  @RequestParam(name = "images") String images,
                                  @RequestParam(name = "info") String info,
                                  @RequestParam(name = "price") Integer price,
                                  @RequestParam(name = "detailedTitle") String detailedTitle,
                                  @RequestParam(name = "detailed") String detailed) {
        ConsoleUpdateVO consoleUpdateVO = new ConsoleUpdateVO();
        try {
            BigInteger result = service.edit(id, name, title, images, info, price, detailedTitle, detailed);
            consoleUpdateVO.setTips(result != null ? "成功" : "失败");
            consoleUpdateVO.setId(id);
        } catch (Exception exception) {
            consoleUpdateVO.setTips(exception.getMessage());
        }
        return consoleUpdateVO;
    }

    @RequestMapping("/product/delete")
    public ConsoleDeleteVO Deleted(@RequestParam(name = "id") BigInteger id){
        int result = service.delete(id);
        ConsoleDeleteVO consoleDeleteVO = new ConsoleDeleteVO();
        consoleDeleteVO.setTips(result  == 1  ? "成功" : "失败");
        return  consoleDeleteVO;
    }
    @RequestMapping("/product/edit")
    public ResponseEntity<?> editProduct(@RequestParam(name = "id") BigInteger id,
                                         @RequestParam(name = "name") String name,
                                         @RequestParam(name = "title") String title,
                                         @RequestParam(name = "images") String images,
                                         @RequestParam(name = "info") String info,
                                         @RequestParam(name = "price") Integer price,
                                         @RequestParam(name = "detailedTitle") String detailedTitle,
                                         @RequestParam(name = "detailed") String detailed){
        try {
            BigInteger result = service.edit(id,name,title,images,info,price,detailedTitle,detailed);
            return  ResponseEntity.ok(result);
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }



}
