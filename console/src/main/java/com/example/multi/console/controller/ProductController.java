package com.example.multi.console.controller;

import com.alibaba.fastjson.JSON;
import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.console.domain.category.ConsoleCreateVO;
import com.example.multi.console.domain.category.ConsoleUpdateVO;
import com.example.multi.console.domain.product.ProductConCellVO;
import com.example.multi.console.domain.product.ProductConInfoVO;
import com.example.multi.console.domain.product.ProductConListVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.productTag.service.ProductTagService;
import com.example.multi.module.tag.service.TagService;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.Response;
import com.example.multi.module.utils.RichTextElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ProductTagService productTagService;


    @RequestMapping("/product/list")
    public Response page(
            @RequestParam("page") Integer page,
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

        return new Response(1001, productConListVO);
    }

    @RequireLogin
    @RequestMapping("/product/info")
    public Response getInfo(
            @RequestParam(name = "id") BigInteger id) {
        Product product = service.getById(id);
        if (product == null) {
            return new Response(3052);
        }

        Category category = categoryService.getById(product.getCategoryId());
        if (category == null) {
            return new Response(3053);
        }

        ProductConInfoVO productConInfoVO = new ProductConInfoVO();
        productConInfoVO.setTitle(product.getTitle());
        String[] image = product.getImages().split("\\$");
        productConInfoVO.setImages(Arrays.asList(image));
        productConInfoVO.setName(product.getName());
        productConInfoVO.setInfo(product.getInfo());
        productConInfoVO.setPrice(product.getPrice());
        productConInfoVO.setDetailedTitle(product.getDetailedTitle());
        //productConInfoVO.setDetailed(product.getDetailed());

        List<RichTextElement> richTextElements = JSON.parseArray(product.getDetailed(), RichTextElement.class);
        productConInfoVO.setContent(richTextElements);
        productConInfoVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getCreateTime()));
        productConInfoVO.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(product.getUpdateTime()));
        productConInfoVO.setCategoryId(product.getCategoryId());

        return new Response(1001, productConInfoVO);
    }


    @RequestMapping("/product/create")
    public Response productCreate(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "images") String images,
            @RequestParam(name = "info") String info,
            @RequestParam(name = "price") Integer price,
            @RequestParam(name = "detailedTitle") String detailedTitle,
            @RequestParam(name = "detailed") String detailed,
            @RequestParam(name = "categoryId") BigInteger categoryId,
            @RequestParam(name = "tags", defaultValue = "") String tags) {
        ConsoleCreateVO consoleCreateVO = new ConsoleCreateVO();

        try {
            // 1. 处理标签
            List<BigInteger> tagIds = tagService.editToTags(tags);

            // 2. 创建商品
            BigInteger productId = service.edit(null, name, title, images, info, price, detailedTitle, detailed, categoryId, tagIds);
            if (productId == null) {
                return new Response(3001); // 商品创建失败
            }
            // 3. 建立商品与标签的关系
            //tagService.manageTags(productId, tagIds);
            // 4. 返回响应
            consoleCreateVO.setId(productId);
            return new Response(1001, consoleCreateVO); // 商品创建成功
        } catch (Exception exception) {
            return new Response(3051, exception.getMessage());
        }
    }



    @RequestMapping("/product/update")
    public Response update(
            @RequestParam(name = "id") BigInteger id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "images") String images,
            @RequestParam(name = "info") String info,
            @RequestParam(name = "price") Integer price,
            @RequestParam(name = "detailedTitle") String detailedTitle,
            @RequestParam(name = "detailed") String detailed,
            @RequestParam(name = "categoryId") BigInteger categoryId,
            @RequestParam(name = "tags", defaultValue = "") String tags) {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        ConsoleUpdateVO consoleUpdateVO = new ConsoleUpdateVO();

        try {
            // 1. 处理标签
            List<BigInteger> tagIds = tagService.editToTags(tags);

            // 2. 更新商品
            BigInteger result = service.edit(id, name, title, images, info, price, detailedTitle, detailed, categoryId, tagIds);
            if (result == null) {
                return new Response(3002); // 商品更新失败
            }

            // 3. 更新商品与标签的关系
            //tagService.manageTags(id, tagIds);

            // 4. 返回响应
            consoleUpdateVO.setId(id);
            return new Response(1001, consoleUpdateVO); // 商品更新成功
        } catch (Exception exception) {
            return new Response(3051, exception.getMessage());
        }
    }

    @RequireLogin
    @RequestMapping("/product/delete")
    public Response deleted(
            @RequestParam(name = "id") BigInteger id) {

        int result = service.delete(id);
        if (result == 1) {
            return new Response(1001);
        } else {
            return new Response(3052);
        }
    }


}
