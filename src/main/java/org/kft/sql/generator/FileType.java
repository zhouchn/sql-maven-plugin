package org.kft.sql.generator;

/**
 * source code file type
 *
 * @author author
 * @since 2024/3/15
 **/
public enum FileType {
    /** file type list */
    ENTITY("entity", "entity.ftl", "entity", ""),
    DTO("dto", "dto.ftl", "dto", "DTO"),
    QUERY("query", "query.ftl", "dto", "Query"),
    MAPPER("mapper", "mapper.ftl", "mapper", "Mapper"),
    SERVICE("service", "service.ftl", "service", "Service"),
    API_SERVICE("apiService", "apiService.ftl", "service", "ApiService"),
    SERVICE_IMPL("serviceImpl", "serviceImpl.ftl", "impl", "ServiceImpl"),
    CONTROLLER("controller", "controller.ftl", "controller", "Controller"),
    ;

    private final String value;
    private final String template;
    private final String location;
    private final String classNameSuffix;

    FileType(String value, String template, String location, String classNameSuffix) {
        this.value = value;
        this.template = template;
        this.location = location;
        this.classNameSuffix = classNameSuffix;
    }

    public String getValue() {
        return value;
    }

    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    public String getLocation() {
        return location;
    }

    public String getTemplate() {
        return template;
    }
}
