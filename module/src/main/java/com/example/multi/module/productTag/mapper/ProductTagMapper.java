package com.example.multi.module.productTag.mapper;

import com.example.multi.module.product.entity.Product;
import com.example.multi.module.productTag.entity.ProductTag;
import com.example.multi.module.tag.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductTagMapper {
    @Select("select * from product_tag where product_id = #{ productId} and is_deleted = 0")
    List<ProductTag> getById(@Param("productId") BigInteger productId);

    @Select("select * from product_tag  where product_id = #{productId} ")
    Tag extractById(@Param("productId") BigInteger productId);

    int update(@Param("productTag") ProductTag ProductTag);

//    int updateByProductId(@Param("productTag")ProductTag productTag);

    int insert(@Param("productTag") ProductTag productTag);

    @Update("update product_tag set is_deleted = 1,update_time = #{time}  where id = #{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);


    @Select("SELECT * FROM product_tag WHERE product_id = #{productId} AND tag_id = #{tagId} AND is_deleted = 1")
    ProductTag getDeletedProductTag(@Param("productId") BigInteger productId, @Param("tagId") BigInteger tagId);

    //@Select("SELECT tag_id FROM product_tag WHERE  WHERE product_id = #{productId} AND is_deleted = 0")
    List<BigInteger> findTagIdsByProductId(BigInteger productId);
}
