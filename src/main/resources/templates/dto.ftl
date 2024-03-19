package ${selfPackage};

<#if enableSwagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
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
 * ${comment!"description"}
 *
 * @author ${author!"author"}
 * @since ${time}
 */
<#if enableLombok>
@Data
@Accessors(chain = true)
</#if>
<#if enableSwagger>
@Schema(name = "${entityName}DTO对象", description = "${comment}")
</#if>
public class ${className}<#if baseClass?? &&baseClass?has_content> extends ${baseClass}</#if> {
<#list columns as col>

  <#if enableSwagger>
    @Schema(description = "${col.comment}")
  </#if>
    private ${col.javaType} ${col.fieldName};
</#list>
}
