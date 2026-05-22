package com.aitest.dashboard.controller;

import com.aitest.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/testplans")
public class TestPlanController {

    private static final List<Map<String, Object>> testPlans = new ArrayList<>();
    private static long idCounter = 4;

    static {
        Map<String, Object> p1 = new HashMap<>();
        p1.put("id", 1L);
        p1.put("name", "Sprint 1 功能测试计划");
        p1.put("description", "第一阶段核心功能测试");
        p1.put("project_id", 1L);
        p1.put("project_name", "电商平台测试项目");
        p1.put("status", "executing");
        p1.put("start_date", "2026-05-10 09:00:00");
        p1.put("end_date", "2026-05-20 18:00:00");
        p1.put("owner", "张三");
        p1.put("created_at", "2026-05-08 10:30:00");
        p1.put("updated_at", "2026-05-10 09:15:00");
        testPlans.add(p1);

        Map<String, Object> p2 = new HashMap<>();
        p2.put("id", 2L);
        p2.put("name", "Sprint 2 回归测试计划");
        p2.put("description", "全量回归测试");
        p2.put("project_id", 1L);
        p2.put("project_name", "电商平台测试项目");
        p2.put("status", "draft");
        p2.put("start_date", "2026-05-21 09:00:00");
        p2.put("end_date", "2026-05-30 18:00:00");
        p2.put("owner", "李四");
        p2.put("created_at", "2026-05-12 14:00:00");
        p2.put("updated_at", "2026-05-12 14:00:00");
        testPlans.add(p2);

        Map<String, Object> p3 = new HashMap<>();
        p3.put("id", 3L);
        p3.put("name", "支付模块专项测试");
        p3.put("description", "支付流程专项测试");
        p3.put("project_id", 3L);
        p3.put("project_name", "支付模块测试");
        p3.put("status", "completed");
        p3.put("start_date", "2026-05-01 09:00:00");
        p3.put("end_date", "2026-05-08 18:00:00");
        p3.put("owner", "王五");
        p3.put("created_at", "2026-04-28 11:20:00");
        p3.put("updated_at", "2026-05-08 19:00:00");
        testPlans.add(p3);
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        List<Map<String, Object>> filtered = testPlans.stream()
                .filter(p -> name == null || name.isEmpty() || ((String) p.get("name")).contains(name))
                .filter(p -> status == null || status.isEmpty() || status.equals(p.get("status")))
                .toList();

        int total = filtered.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Map<String, Object>> pageData = fromIndex < total
                ? filtered.subList(fromIndex, toIndex)
                : new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageData);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Optional<Map<String, Object>> plan = testPlans.stream()
                .filter(p -> id.equals(p.get("id")))
                .findFirst();

        return plan.map(Result::success).orElse(Result.error("测试计划不存在"));
    }

    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> planData) {
        Map<String, Object> newPlan = new HashMap<>(planData);
        newPlan.put("id", idCounter++);
        newPlan.put("created_at", new Date().toString());
        newPlan.put("updated_at", new Date().toString());
        if (!newPlan.containsKey("status")) {
            newPlan.put("status", "draft");
        }
        testPlans.add(newPlan);
        return Result.success(newPlan);
    }

    @PostMapping("/{id}")
    public Result<Map<String, Object>> updateByPost(@PathVariable Long id, @RequestBody Map<String, Object> planData) {
        Optional<Map<String, Object>> existing = testPlans.stream()
                .filter(p -> id.equals(p.get("id")))
                .findFirst();

        if (existing.isPresent()) {
            Map<String, Object> updated = existing.get();
            updated.putAll(planData);
            updated.put("id", id);
            updated.put("updated_at", new Date().toString());
            return Result.success(updated);
        }
        return Result.error("测试计划不存在");
    }

    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> planData) {
        Optional<Map<String, Object>> existing = testPlans.stream()
                .filter(p -> id.equals(p.get("id")))
                .findFirst();

        if (existing.isPresent()) {
            Map<String, Object> updated = existing.get();
            updated.putAll(planData);
            updated.put("id", id);
            updated.put("updated_at", new Date().toString());
            return Result.success(updated);
        }
        return Result.error("测试计划不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean removed = testPlans.removeIf(p -> id.equals(p.get("id")));
        return removed ? Result.success() : Result.error("删除失败");
    }
}
