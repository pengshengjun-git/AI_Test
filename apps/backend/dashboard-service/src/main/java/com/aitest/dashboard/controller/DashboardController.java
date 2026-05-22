package com.aitest.dashboard.controller;

import com.aitest.common.result.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DashboardController() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("projectCount", fetchCount("http://project-service:8003/api/v1/projects?page=1&size=1"));
        
        Map<String, Object> testcaseStats = fetchStats("http://testcase-service:8004/api/v1/testcases/stats");
        if (testcaseStats.isEmpty()) {
            testcaseStats = fetchStats("http://localhost:9004/api/v1/testcases/stats");
        }
        stats.put("testcaseCount", testcaseStats.getOrDefault("total", 0));
        stats.put("aiGeneratedCount", testcaseStats.getOrDefault("aiGenerated", 0));
        
        Map<String, Object> defectStats = fetchStats("http://defect-service:8005/api/v1/defects/stats");
        if (defectStats.isEmpty()) {
            defectStats = fetchStats("http://localhost:9005/api/v1/defects/stats");
        }
        stats.put("defectCount", defectStats.getOrDefault("total", 0));
        
        return Result.success(stats);
    }
    
    private Map<String, Object> fetchStats(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                Object dataObj = result.get("data");
                if (dataObj instanceof Map) {
                    return (Map<String, Object>) dataObj;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching stats from " + url + ": " + e.getMessage());
        }
        return new HashMap<>();
    }

    private long fetchCount(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                Object dataObj = result.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;
                    Object totalObj = data.get("total");
                    if (totalObj instanceof Number) {
                        return ((Number) totalObj).longValue();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching count from " + url + ": " + e.getMessage());
        }
        return 0;
    }

    @GetMapping("/recent-projects")
    public Result<List<Map<String, Object>>> getRecentProjects() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://project-service:8003/api/v1/projects?page=1&size=10", String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                Object dataObj = result.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;
                    Object listObj = data.get("list");
                    if (listObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> projects = (List<Map<String, Object>>) listObj;
                        return Result.success(projects);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(new ArrayList<>());
    }

    @GetMapping("/recent-defects")
    public Result<List<Map<String, Object>>> getRecentDefects() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://defect-service:8005/api/v1/defects?page=1&size=10", String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                Object dataObj = result.get("data");
                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;
                    Object listObj = data.get("list");
                    if (listObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> defects = (List<Map<String, Object>>) listObj;
                        return Result.success(defects);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(new ArrayList<>());
    }

    @GetMapping("/system-status")
    public Result<List<Map<String, String>>> getSystemStatus() {
        List<Map<String, String>> services = new ArrayList<>();
        
        Map<String, String> s1 = new HashMap<>();
        s1.put("name", "认证服务");
        s1.put("status", "online");
        services.add(s1);
        
        Map<String, String> s2 = new HashMap<>();
        s2.put("name", "项目服务");
        s2.put("status", "online");
        services.add(s2);
        
        Map<String, String> s3 = new HashMap<>();
        s3.put("name", "用例服务");
        s3.put("status", "online");
        services.add(s3);
        
        Map<String, String> s4 = new HashMap<>();
        s4.put("name", "AI服务");
        s4.put("status", "online");
        services.add(s4);
        
        return Result.success(services);
    }

    @GetMapping("/todo-list")
    public Result<List<Map<String, Object>>> getTodoList() {
        List<Map<String, Object>> todos = new ArrayList<>();
        
        Map<String, Object> t1 = new HashMap<>();
        t1.put("id", 1);
        t1.put("title", "完成测试计划文档");
        t1.put("completed", false);
        t1.put("deadline", "2026-05-12");
        todos.add(t1);
        
        Map<String, Object> t2 = new HashMap<>();
        t2.put("id", 2);
        t2.put("title", "评审测试用例");
        t2.put("completed", false);
        t2.put("deadline", "2026-05-11");
        todos.add(t2);
        
        Map<String, Object> t3 = new HashMap<>();
        t3.put("id", 3);
        t3.put("title", "执行回归测试");
        t3.put("completed", true);
        t3.put("deadline", "2026-05-10");
        todos.add(t3);
        
        return Result.success(todos);
    }

    @PutMapping("/todo/{id}/status")
    public Result<Map<String, Object>> toggleTodoStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean completed = request.get("completed");
        
        Map<String, Object> todo = new HashMap<>();
        todo.put("id", id);
        todo.put("title", "待办事项");
        todo.put("completed", completed);
        todo.put("deadline", "2026-05-12");
        
        return Result.success(todo);
    }
}
