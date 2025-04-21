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
import java.util.List;


@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id} AND is_deleted=0")
    User getById(BigInteger id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User extractById(BigInteger id);

    int insert(@Param("user") User user);

    int update(@Param("user") User user);

    @Update("update user set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    @Select("SELECT * FROM user WHERE phone = #{phone} AND is_deleted = 0")
    User findUserByPhone(@Param("phone") String phone);

    @Select("select * from user WHERE id in (${ids}) and is_ban = 0 and is_deleted = 0")
    List<User> getByIds(@Param("ids") String ids);

    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode} and is_ban = 0  and is_deleted = 0")
    User getByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);

    @Select("select * from user WHERE phone=#{phone} and country_code=#{countryCode}")
    User extractByPhone(@Param("phone") String phone, @Param("countryCode") String countryCode);

    @Select("select * from user WHERE email=#{email}")
    User extractByEmail(@Param("email") String email);

    @Select("select * from user WHERE username=#{username} and is_ban = 0 and is_deleted = 0")
    User getByUsername(@Param("username") String username);

    @Select("select * from user WHERE wechat_open_id=#{wechatOpenId}")
    User extractUserByWxOpenId(@Param("wechatOpenId") String wechatOpenId);

    @Select("select * from user where username like concat('%',#{username},'%') and is_ban = 0 and is_deleted = 0")
    List<User> getUsersByUsername(@Param("username") String username);

    List<User> getUsersForConsole(@Param("begin") int begin, @Param("size") int size, @Param("orderBy") String orderBy,
                                  @Param("username") String username, @Param("phone") String phone);

    int getUsersTotalForConsole(@Param("username") String username, @Param("phone") String phone);


}
