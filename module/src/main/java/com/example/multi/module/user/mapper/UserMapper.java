package com.example.multi.module.user.mapper;

import com.example.multi.module.product.entity.Product;
import com.example.multi.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;


@Mapper
public interface UserMapper {
    int insert(@Param("user") User user);

    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM user WHERE id = #{id} AND is_deleted = 0")
    User findById(@Param("id") BigInteger id);
}
