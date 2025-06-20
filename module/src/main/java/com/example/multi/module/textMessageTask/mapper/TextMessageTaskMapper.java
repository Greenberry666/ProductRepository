package com.example.multi.module.textMessageTask.mapper;

import com.example.multi.module.textMessageTask.entity.TextMessageTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface TextMessageTaskMapper {

    @Select("select * from text_message_task where id = #{id} and is_deleted = 0")
    TextMessageTask getById(@Param("id") BigInteger id);

    @Select("select * from text_message_task where id = #{id} ")
    TextMessageTask extractById(@Param("id") BigInteger id);

    int update(@Param("messageTask") TextMessageTask messageTask);

    int insert(@Param("messageTask") TextMessageTask messageTask);

    @Update("update text_message_task set is_deleted = 1,update_time = #{time}  where id = #{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);

    @Select("SELECT * FROM text_message_task WHERE status = 'PENDING' AND is_deleted = 0")
    List<TextMessageTask> findPendingTasks();

}
