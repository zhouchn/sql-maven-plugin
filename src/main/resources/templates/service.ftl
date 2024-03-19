package ${selfPackage};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ${entityPackage};
import ${queryPackage};

import java.util.List;

/**
 * ${comment!""}
 *
 * @author ${author!"admin"}
 * @since ${time}
 */
public interface ${entityName}Service extends IService<${entityName}> {
    /**
     * 分页查询接口
     *
     * @param queryParams 查询参数
     */
    IPage<${entityName}> page(${entityName}Query queryParams);

    /**
     * 批量查询接口
     *
     * @param queryParams 查询参数
     */
    List<${entityName}> list(${entityName}Query queryParams);
}
