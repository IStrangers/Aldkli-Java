package com.msw.aldkli;

import com.msw.aldkli.meta.ApiEntry;
import com.msw.aldkli.scanner.ApiScanner;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.msw.aldkli"})
public class AldkliAutoConfiguration implements ApplicationListener {

    private ApplicationContext applicationContext;

    public AldkliAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public AldkliContext aldkliContext() {
        return new AldkliContext();
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        Map<String,ApiEntry> apiEntryMap = this.applicationContext.getBeansOfType(ApiEntry.class);
        if (!apiEntryMap.isEmpty()) {
            ApiScanner apiScanner = new ApiScanner();
            AldkliContext aldkliContext = this.aldkliContext();
            for (ApiEntry apiEntry : apiEntryMap.values()) {
                List apiGroupMetaDataList = apiScanner.scan(apiEntry.getScanPackagePath());
                apiEntry.setApiGroupMetaDataList(apiGroupMetaDataList);
                aldkliContext.addApiEntry(apiEntry);
            }
        }
    }

}
