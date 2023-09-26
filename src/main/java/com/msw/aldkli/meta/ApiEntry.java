package com.msw.aldkli.meta;

import java.util.List;

public class ApiEntry {

    private String name;
    private String scanPackagePath;
    private List<ApiGroupMetaData> apiGroupMetaDataList;

    public ApiEntry(String name, String scanPackagePath) {
        this.name = name;
        this.scanPackagePath = scanPackagePath;
    }

    public String getName() {
        return name;
    }

    public String getScanPackagePath() {
        return scanPackagePath;
    }

    public List<ApiGroupMetaData> getApiGroupMetaDataList() {
        return apiGroupMetaDataList;
    }

    public ApiEntry setApiGroupMetaDataList(List<ApiGroupMetaData> apiGroupMetaDataList) {
        this.apiGroupMetaDataList = apiGroupMetaDataList;
        return this;
    }

}

