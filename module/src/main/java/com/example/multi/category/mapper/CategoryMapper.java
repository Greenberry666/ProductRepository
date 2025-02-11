
package com.example.multi.category.mapper;

import com.example.multi.category.entity.Category;
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface CategoryMapper  {

// 根据ID查询操作
@Select("SELECT * FROM category WHERE id =  #{$Id} AND is_deleted=0")
Category getById(BigInteger id);

// 根据ID提取操作
@Select("SELECT * FROM category WHERE id =  #{$Id}")
Category extractById(BigInteger id);

// 插入操作ck
Integer insert(Category category);

// 更新操作
Integer update(Category category);

// 删除操作
@Update("UPDATE category SET update_time = #{updateTime} , is_deleted = 1 WHERE id = #{id}")
Integer delete(@Param("updateTime") Integer updateTime, @Param("id")  BigInteger id);


List<Category> getCategoryName(@Param("offset") Integer offset,
                               @Param("pageSize") Integer pageSize,
                               @Param("categoryName") String categoryName);


}