package com.example.multi.console.controller;

import com.example.multi.console.domain.*;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
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
            categoryTree.setName(category.getName());
            categoryTreeVO.add(categoryTree);

            // 获取当前类目的所有子类目
            List<Category> childrenCategory = categoryService.getChildrenCategorys();
            if (childrenCategory != null) {
                List<CategoryTreeVO> childrenList = new ArrayList<>();
                for (Category childCategory : childrenCategory) {
                    if (childCategory.getParentId().equals(category.getId())) { // 确保子类目属于当前父类目
                        CategoryTreeVO childTree = new CategoryTreeVO();
                        childTree.setName(childCategory.getName());
                        childrenList.add(childTree);

                        // 递归获取子类目的子类目
                        List<Category> subChildrenCategory = categoryService.getChildrenCategorys();
                        if (subChildrenCategory != null) {
                            List<CategoryTreeVO> grandChildrenList = new ArrayList<>();
                            for (Category subChildCategory : subChildrenCategory) {
                                if (subChildCategory.getParentId().equals(childCategory.getId())) { // 确保子类目属于当前父类目
                                    CategoryTreeVO subChildTree = new CategoryTreeVO();
                                    subChildTree.setName(subChildCategory.getName());
                                    subChildTree.setChildren(new ArrayList<>());
                                    grandChildrenList.add(subChildTree);
                                }
                            }
                            childTree.setChildren(grandChildrenList);
                        }
                    }
                }
                categoryTree.setChildren(childrenList);
            }
        }
        return categoryTreeVO;
    }
}
