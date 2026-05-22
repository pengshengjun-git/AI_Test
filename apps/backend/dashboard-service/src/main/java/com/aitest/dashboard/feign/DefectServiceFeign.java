package com.aitest.dashboard.feign;

import com.aitest.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "defect-service", url = "http://defect-service:8005")
public interface DefectServiceFeign {

    @GetMapping("/api/v1/defects")
    Result<Map<String, Object>> getDefects(@RequestParam(defaultValue = "1") int page, 
                                           @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/v1/defects/count")
    Result<Map<String, Object>> getDefectCount();
}
