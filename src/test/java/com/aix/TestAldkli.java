package com.aix;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msw.aldkli.meta.ApiGroupMetaData;
import com.msw.aldkli.scanner.ApiScanner;

import java.util.List;

public class TestAldkli {

    public static void main(String[] args) throws JsonProcessingException {
        String scanPackage = "com.aix.controller";
        List<ApiGroupMetaData> apiGroupMetaDataList = new ApiScanner().scan(scanPackage);
        System.out.println((new ObjectMapper().writeValueAsString(apiGroupMetaDataList)));
    }

}
