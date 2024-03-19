package ${selfPackage};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${entityPackage};
import ${queryPackage};
import ${mapperPackage};
import ${servicePackage};
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * <description>
 *
 * @author ${author}
 * @since ${time}
 **/
@Component
public class ${entityName}ServiceImpl extends ServiceImpl<${entityName}Mapper, ${entityName}> implements ${entityName}Service {
    @Override
    public IPage<${entityName}> page(${entityName}Query queryParams) {
        Page<${entityName}> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        return getBaseMapper().selectPage(page, buildWrapper(queryParams));
    }

    @Override
    public List<${entityName}> list(${entityName}Query queryParams) {
        return getBaseMapper().selectList(buildWrapper(queryParams));
    }

    private LambdaQueryWrapper<${entityName}> buildWrapper(${entityName}Query query) {
        LambdaQueryWrapper<${entityName}> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.select(
        <#list columns as col>
                        ${entityName}::get${col.fieldName?cap_first},
        </#list>
                        ${entityName}::getId
                );

        <#list columns as col>
        queryWrapper.eq(nonNull(query.get${col.fieldName?cap_first}()), ${entityName}::get${col.fieldName?cap_first}, query.get${col.fieldName?cap_first}());
        </#list>

        queryWrapper.orderByDesc(${entityName}::getId);

        return queryWrapper;
    }
}
