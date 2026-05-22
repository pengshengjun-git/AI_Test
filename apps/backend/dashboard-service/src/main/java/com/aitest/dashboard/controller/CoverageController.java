package com.aitest.dashboard.controller;

import com.aitest.common.result.Result;
import com.aitest.dashboard.dto.CoverageCreateDTO;
import com.aitest.dashboard.dto.CoverageQueryDTO;
import com.aitest.dashboard.entity.Coverage;
import com.aitest.dashboard.service.CoverageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/coverage")
public class CoverageController {

    @Autowired
    private CoverageService coverageService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = coverageService.getCoverageStats();
        return Result.success(stats);
    }

    @GetMapping
    public Result<Map<String, Object>> list(CoverageQueryDTO dto) {
        IPage<Coverage> page = coverageService.queryCoverages(dto);
        Map<String, Object> result = new HashMap<>();
        result.put("list", page.getRecords());
        result.put("total", page.getTotal());
        result.put("page", page.getCurrent());
        result.put("size", page.getSize());
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Coverage> getById(@PathVariable Long id) {
        Coverage coverage = coverageService.getCoverageById(id);
        if (coverage == null) {
            return Result.error("覆盖率记录不存在");
        }
        return Result.success(coverage);
    }

    @PostMapping
    public Result<Coverage> create(@Valid @RequestBody CoverageCreateDTO dto) {
        Coverage coverage = coverageService.createCoverage(dto);
        return Result.success(coverage);
    }

    @PutMapping("/{id}")
    public Result<Coverage> update(@PathVariable Long id, @RequestBody CoverageCreateDTO dto) {
        Coverage coverage = coverageService.updateCoverage(id, dto);
        if (coverage == null) {
            return Result.error("覆盖率记录不存在");
        }
        return Result.success(coverage);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = coverageService.deleteCoverage(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
