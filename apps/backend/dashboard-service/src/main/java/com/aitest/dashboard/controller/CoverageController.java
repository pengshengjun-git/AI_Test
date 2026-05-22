package com.aitest.dashboard.controller;

import com.aitest.common.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/coverage")
public class CoverageController {

    private static final List<Map<String, Object>> coverageList = new ArrayList<>();
    private static long idCounter = 5;

    static {
        Map<String, Object> c1 = new HashMap<>();
        c1.put("id", 1L);
        c1.put("project_id", 1L);
        c1.put("covered_lines", 8500);
        c1.put("total_lines", 10000);
        c1.put("coverage_rate", 85.0);
        c1.put("report_date", "2026-05-10");
        c1.put("created_at", "2026-05-10 18:30:00");
        c1.put("updated_at", "2026-05-10 18:30:00");
        coverageList.add(c1);

        Map<String, Object> c2 = new HashMap<>();
        c2.put("id", 2L);
        c2.put("project_id", 2L);
        c2.put("covered_lines", 6200);
        c2.put("total_lines", 8000);
        c2.put("coverage_rate", 77.5);
        c2.put("report_date", "2026-05-09");
        c2.put("created_at", "2026-05-09 17:20:00");
        c2.put("updated_at", "2026-05-09 17:20:00");
        coverageList.add(c2);

        Map<String, Object> c3 = new HashMap<>();
        c3.put("id", 3L);
        c3.put("project_id", 3L);
        c3.put("covered_lines", 1500);
        c3.put("total_lines", 5000);
        c3.put("coverage_rate", 30.0);
        c3.put("report_date", "2026-05-08");
        c3.put("created_at", "2026-05-08 16:45:00");
        c3.put("updated_at", "2026-05-08 16:45:00");
        coverageList.add(c3);

        Map<String, Object> c4 = new HashMap<>();
        c4.put("id", 4L);
        c4.put("project_id", 4L);
        c4.put("covered_lines", 9000);
        c4.put("total_lines", 9500);
        c4.put("coverage_rate", 94.74);
        c4.put("report_date", "2026-05-07");
        c4.put("created_at", "2026-05-07 15:10:00");
        c4.put("updated_at", "2026-05-07 15:10:00");
        coverageList.add(c4);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        int totalCovered = coverageList.stream()
                .mapToInt(c -> (Integer) c.get("covered_lines"))
                .sum();

        int totalLines = coverageList.stream()
                .mapToInt(c -> (Integer) c.get("total_lines"))
                .sum();

        double avgCoverage = totalLines > 0 ? (double) totalCovered / totalLines * 100 : 0;

        List<Map<String, Object>> history = new ArrayList<>();
        Map<String, Object> h1 = new HashMap<>();
        h1.put("date", "5-6");
        h1.put("rate", 65);
        history.add(h1);

        Map<String, Object> h2 = new HashMap<>();
        h2.put("date", "5-7");
        h2.put("rate", 72);
        history.add(h2);

        Map<String, Object> h3 = new HashMap<>();
        h3.put("date", "5-8");
        h3.put("rate", 80);
        history.add(h3);

        Map<String, Object> h4 = new HashMap<>();
        h4.put("date", "5-9");
        h4.put("rate", 82);
        history.add(h4);

        Map<String, Object> h5 = new HashMap<>();
        h5.put("date", "5-10");
        h5.put("rate", Math.round(avgCoverage * 100.0) / 100.0);
        history.add(h5);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCovered", totalCovered);
        stats.put("totalLines", totalLines > 0 ? totalLines : 10000);
        stats.put("avgCoverage", Math.round(avgCoverage * 100.0) / 100.0);
        stats.put("coverageHistory", history);

        return Result.success(stats);
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) Long project_id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        List<Map<String, Object>> filtered = coverageList.stream()
                .filter(c -> project_id == null || project_id.equals(c.get("project_id")))
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
        Optional<Map<String, Object>> coverage = coverageList.stream()
                .filter(c -> id.equals(c.get("id")))
                .findFirst();

        return coverage.map(Result::success).orElse(Result.error("覆盖率记录不存在"));
    }

    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> coverageData) {
        Map<String, Object> newCoverage = new HashMap<>(coverageData);
        newCoverage.put("id", idCounter++);
        int covered = (Integer) coverageData.getOrDefault("covered_lines", 0);
        int total = (Integer) coverageData.getOrDefault("total_lines", 100);
        double rate = total > 0 ? (double) covered / total * 100 : 0;
        newCoverage.put("coverage_rate", Math.round(rate * 100.0) / 100.0);
        newCoverage.put("created_at", new Date().toString());
        newCoverage.put("updated_at", new Date().toString());
        coverageList.add(newCoverage);
        return Result.success(newCoverage);
    }

    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> coverageData) {
        Optional<Map<String, Object>> existing = coverageList.stream()
                .filter(c -> id.equals(c.get("id")))
                .findFirst();

        if (existing.isPresent()) {
            Map<String, Object> updated = existing.get();
            updated.putAll(coverageData);
            updated.put("id", id);
            int covered = (Integer) updated.getOrDefault("covered_lines", 0);
            int total = (Integer) updated.getOrDefault("total_lines", 100);
            double rate = total > 0 ? (double) covered / total * 100 : 0;
            updated.put("coverage_rate", Math.round(rate * 100.0) / 100.0);
            updated.put("updated_at", new Date().toString());
            return Result.success(updated);
        }
        return Result.error("覆盖率记录不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean removed = coverageList.removeIf(c -> id.equals(c.get("id")));
        return removed ? Result.success() : Result.error("删除失败");
    }
}
