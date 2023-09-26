package com.msw.aldkli.service.impl;

import com.msw.aldkli.AldkliContext;
import com.msw.aldkli.meta.ApiEntry;
import com.msw.aldkli.service.MetaDataService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MetaDataServiceImpl implements MetaDataService {
    private final AldkliContext aldkliContext;

    public List<ApiEntry> getApiEntryList() {
        return this.aldkliContext.getApiEntryList();
    }

    public MetaDataServiceImpl(AldkliContext aldkliContext) {
        this.aldkliContext = aldkliContext;
    }
}
