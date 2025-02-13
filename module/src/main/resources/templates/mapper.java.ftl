<#assign className = table.mapperName>

package ${package.Mapper};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.*;
import java.math.BigInteger;

@Mapper
public interface ${className} {

// 根据ID查询操作
@Select("SELECT * FROM ${table.name} WHERE id =  <#noparse>#{</#noparse>id<#noparse>}</#noparse> AND is_deleted=0")
${entity} getById(BigInteger id);

// 根据ID提取操作
@Select("SELECT * FROM ${table.name} WHERE id =  <#noparse>#{</#noparse>id<#noparse>}</#noparse>")
${entity} extractById(BigInteger id);

// 插入操作
Integer insert(${entity} ${table.name});

// 更新操作
Integer update(${entity} ${table.name});

// 删除操作
@Update("UPDATE ${table.name} SET update_time = <#noparse>#{</#noparse>updateTime<#noparse>}</#noparse> , is_deleted = 1 WHERE id = <#noparse>#{</#noparse>id<#noparse>}</#noparse>")
Integer delete(@Param("updateTime") Integer updateTime, @Param("id")  BigInteger id);


}