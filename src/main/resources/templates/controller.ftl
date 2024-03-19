package ${selfPackage};

import com.baomidou.mybatisplus.core.metadata.IPage;
import ${dtoPackage};
import ${queryPackage};
import ${entityPackage};
import ${servicePackage};
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ${comment!""}
 *
 * @author ${author!"admin"}
 * @since ${time}
 */
@Slf4j
@RestController
@RequestMapping(value = "/xx")
public class ${entityName}Controller {
    @Resource
    private ${entityName}Service ${entityName?uncap_first}Service;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public PageResult<${entityName}DTO> list(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize) {
        ${entityName}Query queryParams = new ${entityName}Query();
        queryParams.setPageNum(pageNum);
        queryParams.setPageSize(pageSize);
        IPage<${entityName}> pageData = ${entityName?uncap_first}Service.page(queryParams);
        List<${entityName}DTO> records = pageData.getRecords().stream().map(item -> {
            ${entityName}DTO dto = new ${entityName}DTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return PageResult.build(records, pageData.getTotal());
    }

    /**
     * 详情查询
     *
     * @return Result
     */
    @GetMapping("/query")
    public Result<${entityName}DTO> query(@RequestParam String id) {
        ${entityName} entityBean = ${entityName?uncap_first}Service.getById(id);
        ${entityName}DTO result = new ${entityName}DTO();
        BeanUtils.copyProperties(entityBean, result);
        return Result.success(result);
    }

    /**
     * 新增
     *
     * @return Result
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody @Valid ${entityName}DTO dto) {
        ${entityName} newEntity = new ${entityName}();
        BeanUtils.copyProperties(dto, newEntity);
        boolean result = ${entityName?uncap_first}Service.save(newEntity);
        return Result.success(result);
    }

    /**
     * 更新
     *
     * @return Result
     */
    @PostMapping("/update")
    public Result<Boolean> updateById(@RequestBody @Valid ${entityName}DTO dto) {
        ${entityName} updateEntity = new ${entityName}();
        BeanUtils.copyProperties(dto, updateEntity);
        boolean result = ${entityName?uncap_first}Service.updateById(updateEntity);
        return Result.success(result);
    }

    /**
     * 删除
     *
     * @return Result
     */
    @PostMapping("/remove")
    public Result<Boolean> removeById(@RequestBody @Valid String id) {
        boolean result = ${entityName?uncap_first}Service.removeById(id);
        return Result.success(result);
    }
}
