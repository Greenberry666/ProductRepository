package com.example.multi.module.textMessage.mapper;

import com.example.multi.module.textMessage.entity.TextMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;

@Mapper
public interface TextMessageMapper {

    @Select("select * from text_message where id = #{id} and is_deleted = 0")
    TextMessage getById(@Param("id") BigInteger id);

    @Select("select * from text_message where id = #{id} ")
    TextMessage extractById(@Param("id") BigInteger id);

    int update(@Param("message") TextMessage message);

    int insert(@Param("message") TextMessage message);

    @Update("update text_message set is_deleted = 1,update_time = #{time}  where id = #{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);


}

