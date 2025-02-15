package com.example.multi.console.controller;

import com.example.multi.console.domain.CategoryCreateVO;
import com.example.multi.console.domain.CategoryDeleteVO;
import com.example.multi.console.domain.CategoryInfoVO;
import com.example.multi.console.domain.CategoryUpdateVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.SimpleDateFormat;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService category;
    @RequestMapping("/category/create")
    public CategoryCreateVO categoryCreate(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "image") String image) {
        CategoryCreateVO categoryCreateVO = new CategoryCreateVO();
        try {
            BigInteger result = category.edit( null,name,  image);
            categoryCreateVO.setTips(result!= null ? "成功" : "失败");
            categoryCreateVO.setId(result);
        } catch (Exception exception) {
            categoryCreateVO.setTips(exception.getMessage());
        }
        return categoryCreateVO;
    }

    @RequestMapping("/category/update")
    public CategoryUpdateVO categoryUpdate(
            @RequestParam(name = "id") BigInteger id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "image") String image) {
        CategoryUpdateVO categoryUpdateVO = new CategoryUpdateVO();
        try {
            BigInteger result = category.edit(id, name, image);
            categoryUpdateVO.setTips(result != null ? "成功" : "失败");
            categoryUpdateVO.setId(result);
        } catch (Exception exception) {
            categoryUpdateVO.setTips(exception.getMessage());
        }
        return categoryUpdateVO;
    }

    @RequestMapping("/category/delete")
    public CategoryDeleteVO categoryDeleted(@RequestParam(name = "id") BigInteger id){
        int result = category.delete(id);
        CategoryDeleteVO categoryDeleteVO = new CategoryDeleteVO();
        categoryDeleteVO.setTips(result  == 1  ? "成功" : "失败");
        return  categoryDeleteVO;
    }

    @RequestMapping("/category/info")
    public CategoryInfoVO categoryInfoVO(@RequestParam(name = "id")BigInteger id){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CategoryInfoVO categoryInfoVO = new CategoryInfoVO();
        try {
        Category categoryInfo = category.getById(id);
        categoryInfoVO .setName(categoryInfo.getName());
        categoryInfoVO.setImage(categoryInfo.getImage());
        categoryInfoVO .setCreateTime(dateFormat.format(categoryInfo.getCreateTime()* 1000l));
        categoryInfoVO .setUpdateTime(dateFormat.format(categoryInfo.getUpdateTime()* 1000l));
        categoryInfoVO.setTips("成功");
        }catch (Exception e){
            categoryInfoVO.setTips(e.getMessage());
        }
        return categoryInfoVO ;

    }
}
