package com.msw.aldkli.meta;

import java.util.List;

public class ApiMetaData {

    private String name;
    private MethodType methodType;
    private List<String> pathList;
    private List<ApiParamMetaData> apiParamMetaDataList;
    private ApiReturnTypeMetaData apiReturnTypeMetaData;

    public ApiMetaData(String name, MethodType methodType, List<String> pathList, List<ApiParamMetaData> apiParamMetaDataList, ApiReturnTypeMetaData apiReturnTypeMetaData) {
        this.name = name;
        this.methodType = methodType;
        this.pathList = pathList;
        this.apiParamMetaDataList = apiParamMetaDataList;
        this.apiReturnTypeMetaData = apiReturnTypeMetaData;
    }

    public String getName() {
        return name;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public List<ApiParamMetaData> getApiParamMetaDataList() {
        return apiParamMetaDataList;
    }

    public ApiReturnTypeMetaData getApiReturnTypeMetaData() {
        return apiReturnTypeMetaData;
    }
}

