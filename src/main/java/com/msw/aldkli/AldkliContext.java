package com.msw.aldkli;

import com.msw.aldkli.meta.ApiEntry;
import java.util.ArrayList;
import java.util.List;

public final class AldkliContext {

    private final List<ApiEntry> apiEntryList = new ArrayList();

    public void addApiEntry(ApiEntry apiEntry) {
        this.apiEntryList.add(apiEntry);
    }

    public List<ApiEntry> getApiEntryList() {
        return this.apiEntryList;
    }

}
