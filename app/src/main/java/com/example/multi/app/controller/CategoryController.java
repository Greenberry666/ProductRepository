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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/category/list")
    public CategoryGeneralListVO getCategoryList() {
        //一级类目
        List<Category> parentCategorys = service.getParentCategorys();

        Map<BigInteger, List<CategoryListCellVO>> childrenMap = new HashMap<>();
        for (Category parentCategory : parentCategorys) {
            //二级类目
            List<Category> childrenCategorys = service.getChildrenCategoryById(parentCategory.getId());
            List<CategoryListCellVO> childrenList = new ArrayList<>();
            for (Category childrenCategory : childrenCategorys) {
                CategoryListCellVO childrenVO = new CategoryListCellVO();
                childrenVO.setCategoryId(childrenCategory.getId());
                childrenVO.setCategoryName(childrenCategory.getName());
                childrenVO.setCategoryImage(childrenCategory.getImage());
                childrenList.add(childrenVO);
            }
            childrenMap.put(parentCategory.getId(), childrenList);
        }

        List<CategoryListVO> parentList = new ArrayList<>();
        for (Category parentCategory : parentCategorys) {
            CategoryListVO parentVO = new CategoryListVO();
            parentVO.setCategoryId(parentCategory.getId());
            parentVO.setCategoryName(parentCategory.getName());
            parentVO.setCategoryImage(parentCategory.getImage());

            // 从Map中取一级类目的子类目
            List<CategoryListCellVO> childrenList = childrenMap.get(parentCategory.getId());
            if (childrenList != null && !childrenList.isEmpty()) {
                parentVO.setChildrenlist(childrenList);
            }
            parentList.add(parentVO);
        }

        CategoryGeneralListVO parentListVO = new CategoryGeneralListVO();
        parentListVO.setList(parentList);

        return parentListVO;
    }


    @GetMapping("/category/children")
    public CategoryChildrenVO getCategoryChildren(@RequestParam BigInteger id) {
        Category currentCategory = service.getById(id);

        CategoryChildrenVO childrenVO = new CategoryChildrenVO();
        childrenVO.setCategoryId(id);
        childrenVO.setCategoryName(currentCategory.getName());
        childrenVO.setCategoryImage(currentCategory.getImage());

        // 子类目列表
        List<CategoryListCellVO> childrenList = new ArrayList<>();
        // 获取所有叶子节点的 ID
        List<BigInteger> leafCategoryIds = new ArrayList<>();
        getLeafCategoryIdsAndChildren(id, leafCategoryIds, childrenList);

        // 将叶子节点的 ID 转换为字符串，用于 SQL 查询
        StringBuilder idList = new StringBuilder();
        for (int i = 0; i < leafCategoryIds.size(); i++) {
            idList.append(leafCategoryIds.get(i));
            if (i < leafCategoryIds.size() - 1) {
                idList.append(",");
            }
        }

        String leafIds = idList.toString();
        // 获取所有叶子节点对应的商品列表
        List<Product> productList = service.getProductByIds(leafIds);

        childrenVO.setChildrenList(childrenList);
        childrenVO.setProductList(productList);

        return childrenVO;
    }

    // 递归获取所有叶子节点的 ID 并填充子类目列表
    private void getLeafCategoryIdsAndChildren(BigInteger parentId, List<BigInteger> leafCategoryIds, List<CategoryListCellVO> childrenList) {
        // 获取当前类目的子类目
        List<Category> children = service.getChildrenCategoryById(parentId);

        if (children.isEmpty()) {
            // 如果没有子类目，当前节点是叶子节点
            leafCategoryIds.add(parentId);
        } else {
            // 如果有子类目，递归处理每个子类目
            for (Category child : children) {
                CategoryListCellVO childVO = new CategoryListCellVO();
                childVO.setCategoryId(child.getId());
                childVO.setCategoryName(child.getName());
                childVO.setCategoryImage(child.getImage());
                childrenList.add(childVO);

                getLeafCategoryIdsAndChildren(child.getId(), leafCategoryIds, childrenList);
            }
        }
    }
}
