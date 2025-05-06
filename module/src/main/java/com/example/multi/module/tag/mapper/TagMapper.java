package com.example.multi.module.tag.mapper;

import com.example.multi.module.tag.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface TagMapper {
    @Select("select * from tag where name = #{name} and is_deleted = 0")
    Tag getByName(@Param("name") String name);

    @Select("select * from tag where name = #{name} ")
    Tag extractByName(@Param("name") String name);

    int update(@Param("tag") Tag tag);

    int insert(@Param("tag") Tag tag);

    @Update("update tag set is_deleted = 1,update_time = #{time}  where name = #{name} limit 1")
    int delete(@Param("name") BigInteger id, @Param("name") Integer time);

//    @Select("SELECT name FROM tag WHERE id IN (${tagIds})")
//    List<String> getTagNamesByIds(@Param("tagIds") String tagIds);

    List<String> getTagNamesByIds(@Param("tagIds") List<BigInteger> tagIds);

}
