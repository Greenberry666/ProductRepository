package com.example.multi.app.controller;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.app.domain.*;
import com.example.multi.module.dto.ProductDTO;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.wp.Wp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryservice;


    @SneakyThrows
    @RequestMapping("/product/upgrade_list")
    public ProductListVO getProductUpgradeList(@RequestParam(value = "wp", defaultValue = "") String wp,
                                               @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Integer page = 1;
        Integer pageSize = 5;
        if (wp != null && !wp.isEmpty()) {
            // Base64 解码
            String decodedWpBase = new String(Base64.getDecoder().decode(wp), "UTF-8");
            // JSON 解码
            Wp decodedWpJSON = JSON.parseObject(decodedWpBase, Wp.class);
            page = decodedWpJSON.getPage();
            //pageSize = decodedWpJSON.getPageSize();
            keyword = decodedWpJSON.getKeyword();
        }

        //List<Product> products = service.getPage(page, pageSize, keyword);
        List<ProductDTO> productDTOS = service.getCategoryAndProductList(page, pageSize, keyword);

        List<ProductCellVO> productCellVOS = new ArrayList<>();
        ImageScaleVO defaultImageScaleVO = new ImageScaleVO();
        defaultImageScaleVO.setImageURL("0");
        defaultImageScaleVO.setAr(0.0);

        for (ProductDTO productdto : productDTOS) {
            //Category category = categoryservice.getById(product.getCategoryId());
            if (productdto.getCategoryName() == null) {
                continue;
            }
            ProductCellVO productCellVO = new ProductCellVO();
            ImageScaleVO imageScaleVO = defaultImageScaleVO;
            productCellVO.setId(productdto.getId());
            String[] imageArray = productdto.getImages().split("\\$");
            //检测图片是否上传
            if (imageArray.length > 0) {
                String imageUrl = imageArray[0];
                imageScaleVO.setImageURL(imageUrl);
                String regex = "(\\d+)x(\\d+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(imageArray[0]);
                //检测图片宽高
                if (matcher.find()) {
                    double width = Integer.parseInt(matcher.group(1));
                    double height = Integer.parseInt(matcher.group(2));
                    //检测图片高度
                    if (height != 0) {
                        imageScaleVO.setAr(width / height);
                    }
                }
            }
            productCellVO.setImage(imageScaleVO);
            productCellVO.setInfo(productdto.getInfo());
            productCellVO.setPrice(productdto.getPrice());
            productCellVO.setCategoryName(productdto.getCategoryName());
            productCellVOS.add(productCellVO);

        }
        ProductListVO productListVO = new ProductListVO();
        productListVO.setIsEnd(productCellVOS.size() < pageSize);
        productListVO.setList(productCellVOS);
        Wp codeByWp = new Wp();
        codeByWp.setPage(page + 1);
        codeByWp.setPageSize(pageSize);
        codeByWp.setKeyword(keyword);
        String jsonInput = JSON.toJSONString(codeByWp);

        String base64Encoded = URLEncoder.encode(Base64.getEncoder().encodeToString(jsonInput.getBytes()));
        productListVO.setWp(base64Encoded);
        return productListVO;

    }

    @SneakyThrows
    @RequestMapping("/product/list")
    public ProductListVO getProductList(@RequestParam(value = "wp", defaultValue = "") String wp,
                                        @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Integer page = 1;
        Integer pageSize = 5;

        if (wp != null && !wp.isEmpty()) {
            // Base64 解码
            String decodedWpBase = new String(Base64.getDecoder().decode(wp), "UTF-8");
            // JSON 解码
            Wp decodedWpJSON = JSON.parseObject(decodedWpBase, Wp.class);
            page = decodedWpJSON.getPage();
            keyword = decodedWpJSON.getKeyword();
        }


        List<Product> products = service.getPage(page, pageSize, keyword);



        List<BigInteger> categoryIds = categoryservice.getAllCategory();
        StringBuilder idList = new StringBuilder();
        for (int i = 0; i < categoryIds.size(); i++) {
            idList.append(categoryIds.get(i));
            if (i < categoryIds.size() - 1) {
                idList.append(",");
            }
        }

        String tagIds = idList.toString();


        List<Category> categories = categoryservice.getByIds(tagIds);
        System.out.println(categories);

        ImageScaleVO defaultImageScaleVO = new ImageScaleVO();
        defaultImageScaleVO.setImageURL("0");
        defaultImageScaleVO.setAr(0.0);


        Map<BigInteger, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }


        List<ProductCellVO> productCellVOS = new ArrayList<>();
        for (Product product : products) {
            BigInteger categoryId = product.getCategoryId();
            String categoryName = categoryMap.get(categoryId);

            if (categoryName == null) {
                continue;
            }

            ProductCellVO productCellVO = new ProductCellVO();
            ImageScaleVO imageScaleVO = defaultImageScaleVO;
            productCellVO.setId(product.getId());
            String[] imageArray = product.getImages().split("\\$");
            //检测图片是否上传
            if (imageArray.length > 0) {
                String imageUrl = imageArray[0];
                imageScaleVO.setImageURL(imageUrl);
                String regex = "(\\d+)x(\\d+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(imageArray[0]);
                //检测图片宽高
                if (matcher.find()) {
                    double width = Integer.parseInt(matcher.group(1));
                    double height = Integer.parseInt(matcher.group(2));
                    //检测图片高度
                    if (height != 0) {
                        imageScaleVO.setAr(width / height);
                    }
                }
            }
            productCellVO.setImage(imageScaleVO);
            productCellVO.setInfo(product.getInfo());
            productCellVO.setPrice(product.getPrice());
            productCellVO.setCategoryName(categoryName);
            productCellVOS.add(productCellVO);
        }


        ProductListVO productListVO = new ProductListVO();
        productListVO.setIsEnd(productCellVOS.size() < pageSize);
        productListVO.setList(productCellVOS);


        Wp codeByWp = new Wp();
        codeByWp.setPage(page + 1);
        codeByWp.setPageSize(pageSize);
        codeByWp.setKeyword(keyword);
        String jsonInput = JSON.toJSONString(codeByWp);
        String base64Encoded = URLEncoder.encode(Base64.getEncoder().encodeToString(jsonInput.getBytes()));
        productListVO.setWp(base64Encoded);

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
