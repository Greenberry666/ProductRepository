<#-- 导入必要的包和定义变量 -->
<#assign className = table.serviceName>
<#assign entityLower = entity?lower_case>  <!-- 将实体类名转换为小写 -->

<#-- 生成 Java 文件内容 -->
package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${entity}Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class ${className} {
@Resource
private ${entity}Mapper mapper;

// 根据ID查询操作
public ${entity} getById(BigInteger id) {
return mapper.getById(id);
}

// 根据ID提取操作
public ${entity} extractById(BigInteger id) {
return mapper.extractById(id);
}

// 插入操作
public int insert(${entity} ${entityLower}) {
return mapper.insert(${entityLower});
}

// 更新操作
public int update(${entity} ${entityLower}) {
return mapper.update(${entityLower});
}

// 删除操作
public int delete(BigInteger id) {
return mapper.delete(BaseUtils.currentSeconds(),id);
}

}