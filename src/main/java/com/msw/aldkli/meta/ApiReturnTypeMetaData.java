package com.msw.aldkli.meta;

import java.util.List;

public class ApiReturnTypeMetaData {

    private String name;
    private String description;
    private String dataType;
    private List<ApiReturnTypeMetaData> children;

    public ApiReturnTypeMetaData(String name, String description, String dataType, List<ApiReturnTypeMetaData> children) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    public List<ApiReturnTypeMetaData> getChildren() {
        return children;
    }
}
