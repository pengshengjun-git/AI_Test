package com.aitest.dashboard.feign;

import com.aitest.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "requirement-service", url = "http://requirement-service:8006")
public interface RequirementServiceFeign {

    @GetMapping("/api/v1/requirements")
    Result<Map<String, Object>> getRequirements(@RequestParam(defaultValue = "1") int page, 
                                                @RequestParam(defaultValue = "10") int size);
}
