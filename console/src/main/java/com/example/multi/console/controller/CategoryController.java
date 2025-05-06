package com.example.multi.console.controller;

import com.example.multi.console.annotation.RequireLogin;
import com.example.multi.console.domain.category.CategoryCreateVO;
import com.example.multi.console.domain.category.CategoryInfoVO;
import com.example.multi.console.domain.category.CategoryTreeVO;
import com.example.multi.console.domain.category.CategoryUpdateVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.user.entity.User;
import com.example.multi.module.utils.BaseUtils;
import com.example.multi.module.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/category/create")
    public Response categoryCreate(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "image") String image) {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        CategoryCreateVO categoryCreateVO = new CategoryCreateVO();
        try {
            BigInteger result = categoryService.edit(null, name, image);
            if (result != null) {
                categoryCreateVO.setId(result);
                return new Response(1001, categoryCreateVO);
            } else {
                return new Response(3053);
            }
        } catch (Exception exception) {
            return new Response(4004);
        }
    }

    @RequestMapping("/category/update")
    public Response categoryUpdate(
            @RequestParam(name = "id") BigInteger id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "image") String image) {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        CategoryUpdateVO categoryUpdateVO = new CategoryUpdateVO();
        try {
            BigInteger result = categoryService.edit(id, name, image);
            if (result != null) {
                categoryUpdateVO.setId(result);
                return new Response(1001, categoryUpdateVO);
            } else {
                return new Response(3053);
            }
        } catch (Exception exception) {
            return new Response(4004);
        }
    }

    @RequestMapping("/category/delete")
    public Response categoryDeleted(
            @RequestParam(name = "id") BigInteger id) {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        int result = categoryService.delete(id);
        if (result == 1) {
            return new Response(1001);
        } else {
            return new Response(3053);
        }
    }

    @RequestMapping("/category/info")
    public Response categoryInfoVO(@RequestParam(name = "id") BigInteger id) {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        Category categoryInfo = categoryService.getById(id);
        if (categoryInfo == null) {
            return new Response(3053);
        }
        CategoryInfoVO categoryInfoVO = new CategoryInfoVO();
        categoryInfoVO.setName(categoryInfo.getName());
        categoryInfoVO.setImage(categoryInfo.getImage());
        categoryInfoVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(categoryInfo.getCreateTime()));
        categoryInfoVO.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(categoryInfo.getUpdateTime()));
        return new Response(1001, categoryInfoVO);
    }

    @RequestMapping("/category/tree")
    public Response getCategoryTree() {
//        if (BaseUtils.isEmpty(loginUser)) {
//            return new Response(1002);
//        }
        List<CategoryTreeVO> categoryTreeVO = new ArrayList<>();
        List<Category> categoryList = categoryService.getParentCategorys();
        for (Category category : categoryList) {
            CategoryTreeVO categoryTree = new CategoryTreeVO();
            categoryTree.setId(category.getId());
            categoryTree.setName(category.getName());
            categoryTree.setImage(category.getImage());
            categoryTree.setChildren(getChildren(category.getId()));
            categoryTreeVO.add(categoryTree);
        }
        return new Response(1001, categoryTreeVO);
    }

    private List<CategoryTreeVO> getChildren(BigInteger id) {
        List<CategoryTreeVO> childrenList = new ArrayList<>();
        List<Category> childrenCategory = categoryService.getChildrenCategoryById(id);
        for (Category childCategory : childrenCategory) {
            CategoryTreeVO childTree = new CategoryTreeVO();
            childTree.setId(childCategory.getId());
            childTree.setName(childCategory.getName());
            childTree.setImage(childCategory.getImage());
            childTree.setChildren(getChildren(childCategory.getId()));
            childrenList.add(childTree);
        }
        return childrenList;
    }
}
