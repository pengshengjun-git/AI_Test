package com.aitest.dashboard.controller;

import com.aitest.common.result.Result;
import com.aitest.dashboard.dto.TestPlanCreateDTO;
import com.aitest.dashboard.dto.TestPlanQueryDTO;
import com.aitest.dashboard.entity.TestPlan;
import com.aitest.dashboard.service.TestPlanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/testplans")
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    @GetMapping
    public Result<Map<String, Object>> list(TestPlanQueryDTO dto) {
        IPage<TestPlan> page = testPlanService.queryTestPlans(dto);
        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("page", page.getCurrent());
        result.put("size", page.getSize());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<TestPlan> getById(@PathVariable Long id) {
        TestPlan testPlan = testPlanService.getTestPlanById(id);
        if (testPlan == null) {
            return Result.error("测试计划不存在");
        }
        return Result.success(testPlan);
    }

    @PostMapping
    public Result<TestPlan> create(@Valid @RequestBody TestPlanCreateDTO dto) {
        TestPlan testPlan = testPlanService.createTestPlan(dto);
        return Result.success(testPlan);
    }

    @PostMapping("/{id}")
    public Result<TestPlan> updateByPost(@PathVariable Long id, @RequestBody TestPlanCreateDTO dto) {
        TestPlan testPlan = testPlanService.updateTestPlan(id, dto);
        if (testPlan == null) {
            return Result.error("测试计划不存在");
        }
        return Result.success(testPlan);
    }

    @PutMapping("/{id}")
    public Result<TestPlan> update(@PathVariable Long id, @RequestBody TestPlanCreateDTO dto) {
        TestPlan testPlan = testPlanService.updateTestPlan(id, dto);
        if (testPlan == null) {
            return Result.error("测试计划不存在");
        }
        return Result.success(testPlan);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = testPlanService.deleteTestPlan(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
