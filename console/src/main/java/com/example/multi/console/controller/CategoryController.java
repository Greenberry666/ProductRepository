package com.example.multi.console.controller;

import com.example.multi.console.domain.*;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    public CategoryCreateVO categoryCreate(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "image") String image) {
        CategoryCreateVO categoryCreateVO = new CategoryCreateVO();
        try {
            BigInteger result = categoryService.edit(null, name, image);
            categoryCreateVO.setTips(result != null ? "成功" : "失败");
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
            BigInteger result = categoryService.edit(id, name, image);
            categoryUpdateVO.setTips(result != null ? "成功" : "失败");
            categoryUpdateVO.setId(result);
        } catch (Exception exception) {
            categoryUpdateVO.setTips(exception.getMessage());
        }
        return categoryUpdateVO;
    }

    @RequestMapping("/category/delete")
    public CategoryDeleteVO categoryDeleted(@RequestParam(name = "id") BigInteger id) {
        int result = categoryService.delete(id);
        CategoryDeleteVO categoryDeleteVO = new CategoryDeleteVO();
        categoryDeleteVO.setTips(result == 1 ? "成功" : "失败");
        return categoryDeleteVO;
    }

    @RequestMapping("/category/info")
    public CategoryInfoVO categoryInfoVO(@RequestParam(name = "id") BigInteger id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CategoryInfoVO categoryInfoVO = new CategoryInfoVO();

        Category categoryInfo = categoryService.getById(id);
        if (categoryInfo == null) {
            categoryInfoVO.setName("未找到对应的分类信息");
            return categoryInfoVO;
        }
        categoryInfoVO.setName(categoryInfo.getName());
        categoryInfoVO.setImage(categoryInfo.getImage());
        categoryInfoVO.setCreateTime(dateFormat.format(categoryInfo.getCreateTime() * 1000l));
        categoryInfoVO.setUpdateTime(dateFormat.format(categoryInfo.getUpdateTime() * 1000l));
        return categoryInfoVO;

    }
    @RequestMapping("/category/tree")
    public List<CategoryTreeVO> getCategoryTree() {
        List<CategoryTreeVO> categoryTreeVO = new ArrayList<>();
        List<Category> categoryList = categoryService.getParentCategorys();

        for (Category category : categoryList) {
            CategoryTreeVO categoryTree = new CategoryTreeVO();
            categoryTree.setId(category.getId());
            categoryTree.setName(category.getName());
            categoryTree.setImage(category.getImage());
            categoryTreeVO.add(categoryTree);

            // 获取当前类目的所有子类目
            List<Category> childrenCategory = categoryService.getChildrenCategoryById(category.getId());

                List<CategoryTreeVO> childrenList = new ArrayList<>();
                for (Category childCategory : childrenCategory) {
                    if (childCategory.getParentId().equals(category.getId())) {
                        CategoryTreeVO childTree = new CategoryTreeVO();
                        childTree.setId(childCategory.getId());
                        childTree.setName(childCategory.getName());
                        childTree.setImage(childCategory.getImage());
                        childrenList.add(childTree);
                        categoryTree.setChildren(childrenList);


                        List<Category> subChildrenCategory = categoryService.getChildrenCategoryById(childCategory.getId());

                        List<CategoryTreeVO> grandChildrenList = new ArrayList<>();
                        for (Category subChildCategory : subChildrenCategory) {
                            if (subChildCategory.getParentId().equals(childCategory.getId())) {
                                CategoryTreeVO subChildTree = new CategoryTreeVO();
                                subChildTree.setId(subChildCategory.getId());
                                subChildTree.setName(subChildCategory.getName());
                                subChildTree.setImage(subChildCategory.getImage());
                                grandChildrenList.add(subChildTree);
                                childTree.setChildren(grandChildrenList);
                            }
                        }
                    }
                }
        }
        return categoryTreeVO;
    }
//递归
//    @RequestMapping("/category/tree")
//    public List<CategoryTreeVO> getCategoryTree() {
//        List<CategoryTreeVO> categoryTreeVO = new ArrayList<>();
//        List<Category> categoryList = categoryService.getParentCategorys();
//
//        for (Category category : categoryList) {
//            if (category == null) {
//                continue;
//            }
//            CategoryTreeVO categoryTree = new CategoryTreeVO();
//            categoryTree.setId(category.getId());
//            categoryTree.setName(category.getName());
//            categoryTree.setImage(category.getImage());
//
//            // 递归获取子类目
//            categoryTree.setChildren(getChildren(category.getId()));
//            categoryTreeVO.add(categoryTree);
//        }
//        return categoryTreeVO;
//    }
//
//    private List<CategoryTreeVO> getChildren(BigInteger id) {
//        List<CategoryTreeVO> childrenList = new ArrayList<>();
//        List<Category> childrenCategory = categoryService.getChildrenCategoryById(id);
//
//        for (Category childCategory : childrenCategory) {
//            if (childCategory == null) {
//                continue;
//            }
//            CategoryTreeVO childTree = new CategoryTreeVO();
//            childTree.setId(childCategory.getId());
//            childTree.setName(childCategory.getName());
//            childTree.setImage(childCategory.getImage());
//
//
//            childTree.setChildren(getChildren(childCategory.getId()));
//            childrenList.add(childTree);
//        }
//        return childrenList;
//    }
}
