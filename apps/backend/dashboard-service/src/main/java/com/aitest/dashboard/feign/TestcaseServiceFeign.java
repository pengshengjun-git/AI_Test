package com.aitest.dashboard.feign;

import com.aitest.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "testcase-service", url = "http://testcase-service:8004")
public interface TestcaseServiceFeign {

    @GetMapping("/api/v1/testcases")
    Result<Map<String, Object>> getTestcases(@RequestParam(defaultValue = "1") int page, 
                                             @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/v1/testcases/count")
    Result<Map<String, Object>> getTestcaseCount();
}
