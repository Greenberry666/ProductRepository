package com.example.multi.product.mapper;
import com.example.multi.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ProductMapper{
    //@Select("select * from product where id=#{id} and is_deleted = 0")
    Product getById(@Param("id") BigInteger id);

    @Select("select * from product where id = #{id}")
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
                         @Param("keyword")String keyword);

}