
package com.example.multi.module.category.mapper;

import com.example.multi.module.category.entity.Category;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface CategoryMapper {

    // 根据ID查询操作
    @Select("SELECT * FROM category WHERE id = #{id} AND is_deleted=0")
    Category getById(BigInteger id);

    // 根据ID提取操作
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category extractById(BigInteger id);

    // 插入操作ck
    int insert(@Param("category") Category category);

    // 更新操作
    int update(@Param("category") Category category);

    // 删除操作
    @Update("UPDATE category SET update_time = #{updateTime} , is_deleted = 1 WHERE id = #{id}")
    Integer delete(@Param("updateTime") Integer updateTime, @Param("id") BigInteger id);


    @Select("select parent_id from category where name like concat ('%', #{categoryName}, '%') and is_deleted = 0  limit 99")
    List<Integer> selectParentId(@Param("categoryName") String categoryName);

    @Select("<script> select * from category where " +
            "<if test= 'categoryName != null '>  name like concat('%',#{categoryName},'%') OR id IN (#{ids}) and </if> " +
            " is_deleted = 0 " +
            " ORDER BY id ASC" +
            " LIMIT #{offset}, #{pageSize} </script>")
    List<Category> getCategoryName(
            @Param("categoryName") String categoryName,
            @Param("ids") String ids,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

//    List<Category> getCategoryName(@Param("offset") Integer offset,
//                         @Param("pageSize") Integer pageSize,
//                         @Param("categoryName") String categoryName);
//
//
    @Select("select * from category where is_deleted = 0")
    List<Category> getCategorys();

    @Select("select * from category where  parent_id is null and is_deleted = 0")
    List<Category> getParentCategorys();

    @Select("select * from category where parent_id is not null and is_deleted = 0")
    List<Category> getChildrenCategorys();

   @Select("SELECT * FROM category WHERE id in (${tagIds})  ")
    List<Category> getIds(@Param("tagIds") String tagIds);

   @Select("select id from category where parent_id is not null and is_deleted = 0")
    List<BigInteger> getAllUsedCategory();




}