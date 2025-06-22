package com.example.multi.app.controller.product;

import com.alibaba.fastjson.JSON;

import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

import com.example.multi.app.annotation.RequireLogin;
import com.example.multi.app.domain.product.ImageScaleVO;
import com.example.multi.app.domain.product.ProductCellVO;
import com.example.multi.app.domain.product.ProductInfoVO;
import com.example.multi.app.domain.product.ProductListVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.dto.ProductDTO;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.product.service.ProductService;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.Response;
import com.example.multi.module.utils.RichTextElement;
import com.example.multi.app.domain.Base.BaseWpVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Duration;


@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryservice;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


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
            BaseWpVO decodedWpJSON = JSON.parseObject(decodedWpBase, BaseWpVO.class);
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
        BaseWpVO codeByWp = new BaseWpVO();
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
//    @Cacheable(value = "categoryListCache", key = "#page + '-' + #keyword", unless = "#result == null")
    public Response getProductList(
            @RequestParam(value = "wp", defaultValue = "") String wp,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Integer page = 1;
        Integer pageSize = 5;

        if (wp != null && !wp.isEmpty()) {
            // Base64 解码
            String decodedWpBase = new String(Base64.getDecoder().decode(wp), "UTF-8");
            // JSON 解码
            BaseWpVO decodedWpJSON = JSON.parseObject(decodedWpBase, BaseWpVO.class);
            page = decodedWpJSON.getPage();
            keyword = decodedWpJSON.getKeyword();
        }
        // 生成缓存key
        String cacheKey = "list_" + page + "_" + keyword;

        // 从Redis中获取缓存数据
        ProductListVO productListVO = (ProductListVO) redisTemplate.opsForValue().get(cacheKey);
        if (productListVO == null) {
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


         productListVO = new ProductListVO();
        productListVO.setIsEnd(productCellVOS.size() < pageSize);
        productListVO.setList(productCellVOS);


        BaseWpVO codeByWp = new BaseWpVO();
        codeByWp.setPage(page + 1);
        codeByWp.setPageSize(pageSize);
        codeByWp.setKeyword(keyword);
        String jsonInput = JSON.toJSONString(codeByWp);
        String base64Encoded = URLEncoder.encode(Base64.getEncoder().encodeToString(jsonInput.getBytes()));
        productListVO.setWp(base64Encoded);
        // 存入Redis缓存，并设置过期时间为60秒
        redisTemplate.opsForValue().set(cacheKey, productListVO,  Duration.ofSeconds(60));
        }
        //return productListVO;
        return new Response(1001, productListVO);
    }



    @RequireLogin
    @RequestMapping("product/info")
    public Response getInfo(@RequestParam(name = "id") BigInteger id) {
        ProductInfoVO productInfoVO = new ProductInfoVO();


        Product product = service.getById(id);
        if (product == null) {
            productInfoVO.setTitle("未找到对应的产品信息");
            return new Response(4004);
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
        List<RichTextElement> richTextElements = JSON.parseArray(product.getDetailed(), RichTextElement.class);

        productInfoVO.setContent(richTextElements);

        List<String> tagNames = service.getTagNamesByProductId(id);
        productInfoVO.setTags(tagNames);
        return new Response(1001, productInfoVO);
    }
}
