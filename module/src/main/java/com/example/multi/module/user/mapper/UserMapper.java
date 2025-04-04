package com.example.multi.module.user.mapper;

import com.example.multi.module.category.entity.Category;
import com.example.multi.module.product.entity.Product;
import com.example.multi.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;


@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id} AND is_deleted=0")
    User getById(BigInteger id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User extractById(BigInteger id);

    int insert(@Param("user") User user);

    int update(@Param("user") User user);

    @Update("UPDATE user SET update_time = #{updateTime} , is_deleted = 1 WHERE id = #{id}")
    Integer delete(@Param("updateTime") Integer updateTime, @Param("id") BigInteger id);

    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findUserByPhone(@Param("phone") String phone);

}
