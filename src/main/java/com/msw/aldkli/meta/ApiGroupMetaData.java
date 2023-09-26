package com.msw.aldkli.meta;

import java.util.List;

public class ApiGroupMetaData {

    private String name;
    private List<String> pathList;
    private List<ApiMetaData> apiMetaDataList;

    public ApiGroupMetaData(String name, List<String> pathList, List<ApiMetaData> apiMetaDataList) {
        this.name = name;
        this.pathList = pathList;
        this.apiMetaDataList = apiMetaDataList;
    }

    public String getName() {
        return name;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public List<ApiMetaData> getApiMetaDataList() {
        return apiMetaDataList;
    }
}
