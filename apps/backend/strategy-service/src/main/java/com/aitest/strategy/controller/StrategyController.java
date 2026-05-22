package com.aitest.strategy.controller;

import com.aitest.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 策略控制器
 */
@RestController
@RequestMapping("/api/v1/strategy")
public class StrategyController {

    // 模拟策略数据
    private static final List<Map<String, Object>> strategies = new ArrayList<>();

    static {
        Map<String, Object> s1 = new HashMap<>();
        s1.put("id", 1);
        s1.put("name", "核心功能冒烟测试");
        s1.put("type", "SMOKE");
        s1.put("description", "每次部署后对核心功能进行快速验证");
        s1.put("priority", "P0");
        s1.put("status", "ENABLED");
        s1.put("creator", "admin");
        s1.put("createTime", "2026-05-01 10:30:00");
        strategies.add(s1);

        Map<String, Object> s2 = new HashMap<>();
        s2.put("id", 2);
        s2.put("name", "完整回归测试策略");
        s2.put("type", "REGRESSION");
        s2.put("description", "发版前执行完整回归测试");
        s2.put("priority", "P1");
        s2.put("status", "ENABLED");
        s2.put("creator", "admin");
        s2.put("createTime", "2026-05-02 14:00:00");
        strategies.add(s2);

        Map<String, Object> s3 = new HashMap<>();
        s3.put("id", 3);
        s3.put("name", "性能基准测试");
        s3.put("type", "PERFORMANCE");
        s3.put("description", "定期执行性能测试");
        s3.put("priority", "P2");
        s3.put("status", "DISABLED");
        s3.put("creator", "tester");
        s3.put("createTime", "2026-05-03 09:00:00");
        strategies.add(s3);
    }

    /**
     * 获取策略列表
     */
    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        List<Map<String, Object>> filtered = strategies.stream()
                .filter(s -> name == null || ((String) s.get("name")).contains(name))
                .filter(s -> type == null || type.isEmpty() || type.equals(s.get("type")))
                .filter(s -> status == null || status.isEmpty() || status.equals(s.get("status")))
                .collect(Collectors.toList());

        int total = filtered.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Map<String, Object>> pageData = fromIndex < total ? 
                filtered.subList(fromIndex, toIndex) : new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageData);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }

    /**
     * 获取策略详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Optional<Map<String, Object>> strategyOpt = strategies.stream()
                .filter(s -> id.equals(s.get("id")))
                .findFirst();
        
        if (strategyOpt.isPresent()) {
            return Result.success(strategyOpt.get());
        }
        return Result.fail("策略不存在");
    }

    /**
     * 创建策略
     */
    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> strategy) {
        Map<String, Object> newStrategy = new HashMap<>(strategy);
        newStrategy.put("id", System.currentTimeMillis());
        newStrategy.put("creator", "admin");
        newStrategy.put("createTime", new Date().toString());
        if (!newStrategy.containsKey("status")) {
            newStrategy.put("status", "ENABLED");
        }
        strategies.add(newStrategy);
        return Result.success(newStrategy);
    }

    /**
     * 更新策略
     */
    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> strategy) {
        Optional<Map<String, Object>> existing = strategies.stream()
                .filter(s -> id.equals(s.get("id")))
                .findFirst();

        if (existing.isPresent()) {
            Map<String, Object> updated = existing.get();
            updated.putAll(strategy);
            updated.put("id", id); // 确保ID不被覆盖
            return Result.success(updated);
        }
        return Result.fail("策略不存在");
    }

    /**
     * 删除策略
     */
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> delete(@PathVariable Long id) {
        boolean removed = strategies.removeIf(s -> id.equals(s.get("id")));
        if (removed) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "删除成功");
            return Result.success(result);
        }
        return Result.fail("策略不存在");
    }

    /**
     * 切换策略状态
     */
    @PutMapping("/{id}/status")
    public Result<Map<String, Object>> toggleStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        
        Optional<Map<String, Object>> existing = strategies.stream()
                .filter(s -> id.equals(s.get("id")))
                .findFirst();

        if (existing.isPresent()) {
            Map<String, Object> strategy = existing.get();
            strategy.put("status", newStatus);
            return Result.success(strategy);
        }
        return Result.fail("策略不存在");
    }
}
