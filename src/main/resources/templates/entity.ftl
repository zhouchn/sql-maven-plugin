package ${selfPackage};

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
<#if enableLombok>
import lombok.Data;
import lombok.experimental.Accessors;
</#if>

<#list columns as col>
  <#if col.javaType == "LocalDateTime">
import java.time.LocalDateTime;
    <#break>
  </#if>
</#list>

/**
 * ${comment!""}
 *
 * @author ${author!"admin"}
 * @since ${time}
 */
<#if enableLombok>
@Data
@Accessors(chain = true)
</#if>
@TableName(value = "${tableName}", autoResultMap = true)
public class ${className}<#if baseClass?? &&baseClass?has_content> extends ${baseClass}</#if> {
<#list columns as col>

    /**
     * ${col.comment}
     */
    @TableField("${col.name}")
    private ${col.javaType} ${col.fieldName};
</#list>
}
