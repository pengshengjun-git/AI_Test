package com.aitest.testcase.controller;

import com.aitest.common.result.Result;
import com.aitest.testcase.dto.TestcaseCreateDTO;
import com.aitest.testcase.dto.TestcaseQueryDTO;
import com.aitest.testcase.dto.TestcaseUpdateDTO;
import com.aitest.testcase.entity.Testcase;
import com.aitest.testcase.entity.TestcaseStep;
import com.aitest.testcase.service.TestcaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用例管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/testcases")
public class TestcaseController {

    @Autowired
    private TestcaseService testcaseService;

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "testcase-service");
        return Result.success(data);
    }

    /**
     * 查询测试用例列表（兼容前端调用）
     */
    @GetMapping
    public Result<Map<String, Object>> listTestcases(TestcaseQueryDTO dto) {
        IPage<Testcase> page = testcaseService.queryTestcases(dto);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        data.put("page", page.getCurrent());
        data.put("size", page.getSize());
        return Result.success(data);
    }

    /**
     * 创建测试用例
     */
    @PostMapping
    public Result<Testcase> createTestcase(@Valid @RequestBody TestcaseCreateDTO dto) {
        try {
            Testcase testcase = testcaseService.createTestcase(dto);
            return Result.success(testcase);
        } catch (Exception e) {
            log.error("创建测试用例失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新测试用例（POST方式，兼容前端调用）
     */
    @PostMapping("/{id}")
    public Result<Testcase> updateTestcaseByPost(@PathVariable Long id, @Valid @RequestBody TestcaseUpdateDTO dto) {
        dto.setId(id);
        Testcase testcase = testcaseService.updateTestcase(dto);
        return Result.success(testcase);
    }

    /**
     * 更新测试用例（PUT方式）
     */
    @PutMapping("/{id}")
    public Result<Testcase> updateTestcase(@PathVariable Long id, @Valid @RequestBody TestcaseUpdateDTO dto) {
        dto.setId(id);
        Testcase testcase = testcaseService.updateTestcase(dto);
        return Result.success(testcase);
    }

    /**
     * 删除测试用例
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTestcase(@PathVariable Long id) {
        boolean success = testcaseService.deleteTestcase(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除测试用例
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteTestcases(@RequestBody List<Long> ids) {
        boolean success = testcaseService.batchDeleteTestcases(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 获取用例统计
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStatistics(@RequestParam(required = false) Long projectId) {
        Map<String, Object> stats = testcaseService.getStatistics(projectId);
        return Result.success(stats);
    }

    /**
     * 获取测试用例详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getTestcaseDetail(@PathVariable Long id) {
        Testcase testcase = testcaseService.getTestcaseDetail(id);
        List<TestcaseStep> steps = testcaseService.getTestcaseSteps(id);

        Map<String, Object> data = new HashMap<>();
        data.put("testcase", testcase);
        data.put("steps", steps);
        return Result.success(data);
    }

    /**
     * 分页查询测试用例列表
     */
    @GetMapping("/page")
    public Result<IPage<Testcase>> queryTestcases(TestcaseQueryDTO dto) {
        IPage<Testcase> page = testcaseService.queryTestcases(dto);
        return Result.success(page);
    }

    /**
     * 查询项目下的所有测试用例
     */
    @GetMapping("/project/{projectId}")
    public Result<List<Testcase>> getTestcasesByProjectId(@PathVariable Long projectId) {
        List<Testcase> list = testcaseService.getTestcasesByProjectId(projectId);
        return Result.success(list);
    }

    /**
     * 查询需求下的所有测试用例
     */
    @GetMapping("/requirement/{requirementId}")
    public Result<List<Testcase>> getTestcasesByRequirementId(@PathVariable Long requirementId) {
        List<Testcase> list = testcaseService.getTestcasesByRequirementId(requirementId);
        return Result.success(list);
    }

    /**
     * 获取用例步骤列表
     */
    @GetMapping("/{id}/steps")
    public Result<List<TestcaseStep>> getTestcaseSteps(@PathVariable Long id) {
        List<TestcaseStep> steps = testcaseService.getTestcaseSteps(id);
        return Result.success(steps);
    }

    /**
     * 添加标签
     */
    @PostMapping("/{id}/tags")
    public Result<Testcase> addTags(@PathVariable Long id, @RequestBody List<String> tags) {
        testcaseService.addTags(id, tags);
        Testcase testcase = testcaseService.getById(id);
        return Result.success(testcase);
    }

    /**
     * 移除标签
     */
    @DeleteMapping("/{id}/tags/{tag}")
    public Result<Testcase> removeTag(@PathVariable Long id, @PathVariable String tag) {
        testcaseService.removeTag(id, tag);
        Testcase testcase = testcaseService.getById(id);
        return Result.success(testcase);
    }
}
