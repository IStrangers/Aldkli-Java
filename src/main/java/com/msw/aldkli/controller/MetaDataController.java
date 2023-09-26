package com.msw.aldkli.controller;

import com.msw.aldkli.meta.ApiEntry;
import com.msw.aldkli.service.MetaDataService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"aldkli/metaData"})
public final class MetaDataController {
    private MetaDataService metaDataService;

    public MetaDataController(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @GetMapping({"getApiEntryList"})
    public List<ApiEntry> getApiEntryList() {
        return this.metaDataService.getApiEntryList();
    }

}
