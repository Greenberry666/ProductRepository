package com.example.multi.module.product.mapper;

import com.example.multi.module.category.entity.Category;
import com.example.multi.module.product.entity.Product;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("select * from product where id = #{id} and is_deleted = 0")
    Product getById(@Param("id") BigInteger id);

    @Select("select * from product where id = #{id} ")
    Product extractById(@Param("id") BigInteger id);

    int update(@Param("product") Product product);

    int insert(@Param("product") Product product);

    @Update("update product set is_deleted = 1,update_time = #{time}  where id = #{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    @Select("select * from product where is_deleted = 0")
    List<Product> getAllProduct();

    @Select("select count(*) from product  where is_deleted = 0 " +
            "and title like concat('%',#{keyword},'%')")
    int pageCount(@Param("keyword") String keyword);


    List<Product> select(@Param("offset") Integer offset,
                         @Param("pageSize") Integer pageSize,
                         @Param("keyword") String keyword);

    @Select("select id from category where name LIKE CONCAT('%', #{keyword}, '%') and is_deleted = 0")
    List<Integer> getProductIds(@Param("keyword") String keyword);


//    @Select("SELECT * FROM product " +
//            "WHERE (title LIKE CONCAT('%', #{keyword}, '%') OR category_id IN (#{ids}))" +
//            "AND is_deleted = 0" +
//            "ORDER BY id ASC" +
//            "LIMIT #{offset}, #{pageSize}")
    @Select("<script> select * from product where " +
            "<if test= 'keyword != null '>  title like concat('%',#{keyword},'%') OR category_id IN (#{ids}) and </if> " +
            " is_deleted = 0 " +
            " ORDER BY id ASC" +
            " LIMIT #{offset}, #{pageSize} </script>")
    List<Product> getProducts(
            @Param("keyword") String keyword,
            @Param("ids") String ids,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);














// select * from product
// title like concat('%',#{keyword},'%')
//  OR category_id IN (
//                    SELECT id FROM category
//                    WHERE name LIKE CONCAT('%', #{keyword}, '%') and is_deleted = 0
//                     )
//      and
//    is_deleted = 0
//    ORDER BY id ASC
//    LIMIT #{offset}, #{pageSize}
@Select("select * from category WHERE name like CONCAT('%', #{keyword}, '%') and is_deleted = 0  limit 99")
List<Integer> selectIdByCategory(@Param("keyword") String keyword);

//    @Select("<script>" +
//            "SELECT * FROM product " +
//            "WHERE is_deleted = 0 " +
//            "AND (" +
//            "title LIKE CONCAT('%', #{keyword}, '%') " +
//            "<if test='categoryIds != null and !categoryIds.isEmpty()'> " +
//            "OR category_id IN " +
//            "<foreach item='id' collection='categoryIds' open='(' separator=',' close=')'>" +
//            "#{id}" +
//            "</foreach>" +
//            "</if>" +
//            ")" +
//            "ORDER BY id ASC " +
//            "LIMIT #{offset}, #{pageSize}" +
//            "</script>")
//    @Select("<script> select * from where " +
//            "<if test='categoryIds != null and !categoryIds.isEmpty()'>" +
//            " (title LIKE CONCAT('%', #{keyword}, '%') OR category_id IN " +
//            "<foreach item='id' collection='categoryIds' open='(' separator=',' close=')'> #{id} </foreach>)and</if>" +
//            "is_deleted = 0 ORDER BY id ASC LIMIT #{offset}, #{pageSize}" +
//            " </script>")
//    List<Product> selectProducts(@Param("keyword") String keyword,
//                                 @Param("offset") Integer offset,
//                                 @Param("categoryIds") List<Integer> categoryIds,
//                                 @Param("pageSize") Integer pageSize);






}