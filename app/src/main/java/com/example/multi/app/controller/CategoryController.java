package com.example.multi.app.controller;

import com.example.multi.app.domain.CategoryGeneralListVO;
import com.example.multi.app.domain.CategoryListCellVO;
import com.example.multi.app.domain.CategoryListVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;

    @RequestMapping("/category/list")
    public CategoryGeneralListVO getCategory(@RequestParam("page") Integer page,
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                             @RequestParam(value = "categoryName", defaultValue = "") String categoryName) {


        List<Category> categorys = service.getCategory(page, pageSize, categoryName);

        List<CategoryListVO> categoryListVOS = new ArrayList<>();
        for (Category generalcategory : categorys){
            CategoryListVO categoryListVO = new CategoryListVO();
            categoryListVO.setCategoryId(generalcategory.getId());
            categoryListVO.setCategoryName(generalcategory.getName());
            categoryListVO.setCategoryImage(generalcategory.getImage());

            List<CategoryListCellVO> productCateCellVOS = new ArrayList<>();
            for (Category category : categorys) {
                CategoryListCellVO productCateCellVO = new CategoryListCellVO();
                productCateCellVO.setCategoryId(category.getId());
                productCateCellVO.setCategoryName(category.getName());
                productCateCellVO.setCategoryImage(category.getImage());
                productCateCellVOS.add(productCateCellVO);
            }
            categoryListVO.setList(productCateCellVOS);
            categoryListVOS.add(categoryListVO);

        }
        CategoryGeneralListVO categoryGeneralListVO = new CategoryGeneralListVO();
        categoryGeneralListVO.setList(categoryListVOS);

        boolean result = categoryListVOS.size() < pageSize;
        categoryGeneralListVO.setIsEnd(result);
        return categoryGeneralListVO;


//        List<CategoryListVO> categoryListVOS = new ArrayList<>();
//        for (Category generalCategory : categorys) {
//            CategoryListVO categoryListVO = new CategoryListVO();
//            categoryListVO.setCategoryId(generalCategory.getId());
//            categoryListVO.setCategoryName(generalCategory.getName());
//            categoryListVO.setCategoryImage(generalCategory.getImage());
//
//            // 获取子分类并封装
//            List<CategoryListCellVO> childrenList = new ArrayList<>();
//            for (Category child : generalCategory.getChildren()) {
//                CategoryListCellVO childVO = new CategoryListCellVO();
//                childVO.setCategoryId(child.getId());
//                childVO.setCategoryName(child.getName());
//                childVO.setCategoryImage(child.getImage());
//                childrenList.add(childVO);
//            }
//            categoryListVO.setList(childrenList);
//            categoryListVOS.add(categoryListVO);
//        }
//        CategoryGeneralListVO categoryGeneralListVO = new CategoryGeneralListVO();
//        categoryGeneralListVO.setList(categoryListVOS);
//        categoryGeneralListVO.setIsEnd(categoryListVOS.size() < pageSize);
//
//        return categoryGeneralListVO;


    }


}
