package com.example.multi.app.controller;

import com.example.multi.app.domain.CategoryChildrenVO;
import com.example.multi.app.domain.CategoryGeneralListVO;
import com.example.multi.app.domain.CategoryListCellVO;
import com.example.multi.app.domain.CategoryListVO;
import com.example.multi.module.category.entity.Category;
import com.example.multi.module.category.service.CategoryService;
import com.example.multi.module.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    //    @RequestMapping("/category/list")
//    public CategoryGeneralListVO getCategoryList() {
//        //一级类目
//        List<Category> parentCategorys = service.getParentCategorys();
//
//        List<Category> childrenCategorys = service.getChildrenCategorys();
//
//        //一级类目列表
//        List<CategoryListVO> parentList = new ArrayList<>();
//        for (Category parentCategory : parentCategorys) {
//            CategoryListVO parentVO = new CategoryListVO();
//            parentVO.setCategoryId(parentCategory.getId());
//            parentVO.setCategoryName(parentCategory.getName());
//            parentVO.setCategoryImage(parentCategory.getImage());
//
//            // 获取一级类目的二级类目
//            List<CategoryListCellVO> childrenList = new ArrayList<>();
//
//            for (Category childrenCategory : childrenCategorys) {
//
//                if (childrenCategory.getParentId().equals(parentCategory.getId())) {
//
//                    CategoryListCellVO childrenVO = new CategoryListCellVO();
//                    childrenVO.setCategoryId(childrenCategory.getId());
//                    childrenVO.setCategoryName(childrenCategory.getName());
//                    childrenVO.setCategoryImage(childrenCategory.getImage());
//                    childrenList.add(childrenVO);
//                    // 获取二级类目的三级类目
//                    List<Category> grandChildrenCategorys = service.getChildrenCategorys();
//                    List<CategoryListCellVO> grandChildrenList = new ArrayList<>();
//                    for (Category grandchildCategory : grandChildrenCategorys) {
//                        if (grandchildCategory.getParentId().equals(childrenCategory.getId())) {
//                            CategoryListCellVO grandchildVO = new CategoryListCellVO();
//                            grandchildVO.setCategoryId(grandchildCategory.getId());
//                            grandchildVO.setCategoryName(grandchildCategory.getName());
//                            grandchildVO.setCategoryImage(grandchildCategory.getImage());
//                            grandChildrenList.add(grandchildVO);
//                            childrenVO.setChildrenlist(grandChildrenList);
//                        }
//                    }
//                }
//            }
//            parentVO.setChildrenlist(childrenList);
//            parentList.add(parentVO);
//        }
//        CategoryGeneralListVO parentListVO = new CategoryGeneralListVO();
//        parentListVO.setList(parentList);
//
//        return parentListVO;
//    }
    // 一级和二级类目接口
    @GetMapping("/category/list")
    public CategoryGeneralListVO getCategoryList() {
        // 获取一级类目
        List<Category> parentCategorys = service.getParentCategorys();

        // 获取所有二级类目
        List<Category> childrenCategorys = service.getChildrenCategorys();

        // 构建一级类目列表
        List<CategoryListVO> parentList = new ArrayList<>();
        for (Category parentCategory : parentCategorys) {
            CategoryListVO parentVO = new CategoryListVO();
            parentVO.setCategoryId(parentCategory.getId());
            parentVO.setCategoryName(parentCategory.getName());
            parentVO.setCategoryImage(parentCategory.getImage());

            // 获取一级类目的二级类目
            List<CategoryListCellVO> childrenList = new ArrayList<>();
            for (Category childrenCategory : childrenCategorys) {
                if (childrenCategory.getParentId().equals(parentCategory.getId())) {
                    CategoryListCellVO childrenVO = new CategoryListCellVO();
                    childrenVO.setCategoryId(childrenCategory.getId());
                    childrenVO.setCategoryName(childrenCategory.getName());
                    childrenVO.setCategoryImage(childrenCategory.getImage());
                    childrenList.add(childrenVO);
                }
            }
            if (!childrenList.isEmpty()) {
                parentVO.setChildrenlist(childrenList);
            }
            parentList.add(parentVO);
        }

        // 构建返回对象
        CategoryGeneralListVO parentListVO = new CategoryGeneralListVO();
        parentListVO.setList(parentList);

        return parentListVO;
    }

    // 三级及以上类目接口
//    @GetMapping("/category/children")
//    public CategoryChildrenVO getCategoryChildren(@RequestParam BigInteger id) {
//        // 获取当前类目的子类目
//        List<Category> childrenCategorys = service.getChildrenCategoryById(id);
//
//        List<BigInteger> productIds = service. getChildrenCategoryByIds(id);
//        StringBuilder idList = new StringBuilder();
//        for (int i = 0; i < productIds.size(); i++) {
//            idList.append(productIds.get(i));
//            if (i < productIds.size() - 1) {
//                idList.append(",");
//            }
//        }
//
//        String productByIds = idList.toString();
//
//
//        // 获取当前类目下的商品列表
//        List<Product> productList = service.getProductByIds(productByIds);
//
//        // 创建返回对象
//        CategoryChildrenVO childrenVO = new CategoryChildrenVO();
//
//        childrenVO.setCategoryId(id);
//
//
//        // 转换子类目为DTO
//        List<CategoryListCellVO> childrenList = new ArrayList<>();
//        for (Category childrenCategory : childrenCategorys) {
//            CategoryListCellVO childVO = new CategoryListCellVO();
//            childVO.setCategoryId(childrenCategory.getId());
//            childVO.setCategoryName(childrenCategory.getName());
//            childVO.setCategoryImage(childrenCategory.getImage());
//            childrenList.add(childVO);
//        }
//        childrenVO.setChildrenlist(childrenList);
//
//        // 设置商品列表
//        childrenVO.setProductList(productList);
//
//        return childrenVO;
//    }
    @GetMapping("/category/children")
    public CategoryChildrenVO getCategoryChildren(@RequestParam BigInteger id) {
        Category currentCategory = service.getById(id);
        // 获取当前类目的子类目
        List<Category> childrenCategorys = service.getChildrenCategoryById(id);

        // 如果没有子类目，直接获取当前类目下的商品
        List<Product> productList;
        if (childrenCategorys.isEmpty()) {
            productList = service.getProductsByCategoryId(id);
        } else {
            // 如果有子类目，获取所有子类目的ID
            List<BigInteger> productIds = service.getChildrenCategoryByIds(id);
            StringBuilder idList = new StringBuilder();
            for (int i = 0; i < productIds.size(); i++) {
                idList.append(productIds.get(i));
                if (i < productIds.size() - 1) {
                    idList.append(",");
                }
            }

            String productByIds = idList.toString();
            // 获取子类目下的商品列表
            productList = service.getProductByIds(productByIds);
        }

        // 创建返回对象
        CategoryChildrenVO childrenVO = new CategoryChildrenVO();
        childrenVO.setCategoryId(id);
        childrenVO.setCategoryName(currentCategory.getName());
        childrenVO.setCategoryImage(currentCategory.getImage());

        // 转换子类目为DTO
        List<CategoryListCellVO> childrenList = new ArrayList<>();
        for (Category childrenCategory : childrenCategorys) {
            CategoryListCellVO childVO = new CategoryListCellVO();
            childVO.setCategoryId(childrenCategory.getId());
            childVO.setCategoryName(childrenCategory.getName());
            childVO.setCategoryImage(childrenCategory.getImage());
            childrenList.add(childVO);
        }
        childrenVO.setChildrenlist(childrenList);

        // 设置商品列表
        childrenVO.setProductList(productList);

        return childrenVO;
    }
}
