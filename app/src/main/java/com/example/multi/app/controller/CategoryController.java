package com.example.multi.app.controller;

import com.example.multi.app.domain.CategoryListCellVO;
import com.example.multi.app.domain.CategoryListVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;
    @RequestMapping("/category/list")
    public CategoryListVO getCategory(@RequestParam("page") Integer page,
                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                      @RequestParam(value = "categoryName", defaultValue = "") String categoryName) {
        List<Category> categorys = service.getCategory(page,pageSize,categoryName);
        List<CategoryListCellVO> productCateCellVOS = new ArrayList<>();
        for (Category category : categorys) {
            CategoryListCellVO productCateCellVO = new CategoryListCellVO();
            productCateCellVO.setCategoryName(category.getName());
            productCateCellVO.setCategoryImage(category.getImage());
            productCateCellVOS.add(productCateCellVO);
        }
        CategoryListVO productCategoryVO = new CategoryListVO();
        productCategoryVO.setList(productCateCellVOS);
        boolean result = productCateCellVOS.size() < pageSize;
        productCategoryVO.setIsEnd(result);
        return productCategoryVO;

    }
}
