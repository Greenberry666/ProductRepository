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
    public CategoryGeneralListVO getCategory() {
        List<Category> parentCategorys = service.getParentCategorys();
        List<Category> childrenCategorys = service.getChildrenCategorys();

        List<CategoryListVO> parentList = new ArrayList<>();
        for (Category parentCategory : parentCategorys) {
            CategoryListVO parentVO = new CategoryListVO();
            parentVO.setCategoryId(parentCategory.getId());
            parentVO.setCategoryName(parentCategory.getName());
            parentVO.setCategoryImage(parentCategory.getImage());

            List<CategoryListCellVO> childrenList = new ArrayList<>();
            for (Category childrenCategory : childrenCategorys) {
                CategoryListCellVO childrenVO = new CategoryListCellVO();

                if (childrenCategory.getParentId().equals(parentCategory.getId())) {
                    childrenVO.setCategoryId(childrenCategory.getId());
                    childrenVO.setCategoryName(childrenCategory.getName());
                    childrenVO.setCategoryImage(childrenCategory.getImage());
                    List<CategoryListCellVO> grandChildrenList = new ArrayList<>();
                    List<Category> grandChildrenCategorys = service.getChildrenCategorys();
                    for (Category grandchildCategory : grandChildrenCategorys) {
                        if (grandchildCategory.getParentId().equals(childrenCategory.getId())) {
                            CategoryListCellVO grandchildVO = new CategoryListCellVO();
                            grandchildVO.setCategoryId(grandchildCategory.getId());
                            grandchildVO.setCategoryName(grandchildCategory.getName());
                            grandchildVO.setCategoryImage(grandchildCategory.getImage());
                            grandchildVO.setChildrenList(new ArrayList<>());
                            grandChildrenList.add(grandchildVO);
                        }
                    }
                    childrenVO.setChildrenList(grandChildrenList);
                    childrenList.add(childrenVO);
                }
            }
            parentVO.setChildrenlist(childrenList);
            parentList.add(parentVO);

        }
        CategoryGeneralListVO parentListVO = new CategoryGeneralListVO();
        parentListVO.setList(parentList);

        return parentListVO;


    }


}
