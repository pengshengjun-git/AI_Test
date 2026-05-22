package com.aitest.dashboard.feign;

import com.aitest.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "project-service", url = "http://project-service:8003")
public interface ProjectServiceFeign {

    @GetMapping("/api/v1/projects")
    Result<Map<String, Object>> getProjects(@RequestParam(defaultValue = "1") int page, 
                                             @RequestParam(defaultValue = "10") int size);

    @GetMapping("/api/v1/projects/count")
    Result<Map<String, Object>> getProjectCount();
}
