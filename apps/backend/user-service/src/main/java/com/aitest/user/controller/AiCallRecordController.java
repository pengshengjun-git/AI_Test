package com.aitest.user.controller;

import com.aitest.common.result.Result;
import com.aitest.user.dto.AiCallRecordQueryDTO;
import com.aitest.user.dto.AiCallRecordVO;
import com.aitest.user.dto.AiStatisticsVO;
import com.aitest.user.service.AiCallRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI调用记录控制器
 */
@RestController
@RequestMapping("/api/ai-call-records")
public class AiCallRecordController {

    private final AiCallRecordService aiCallRecordService;

    public AiCallRecordController(AiCallRecordService aiCallRecordService) {
        this.aiCallRecordService = aiCallRecordService;
    }

    /**
     * 分页查询调用记录
     */
    @GetMapping
    public Result<IPage<AiCallRecordVO>> listRecords(AiCallRecordQueryDTO query) {
        IPage<AiCallRecordVO> page = aiCallRecordService.queryRecords(query);
        return Result.success(page);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result<AiStatisticsVO> getStatistics(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        AiStatisticsVO statistics = aiCallRecordService.getStatistics(userId, startTime, endTime);
        return Result.success(statistics);
    }

    /**
     * 获取模型列表
     */
    @GetMapping("/models")
    public Result<List<String>> getModelNames() {
        List<String> models = aiCallRecordService.getModelNames();
        return Result.success(models);
    }

    /**
     * 获取功能类型列表
     */
    @GetMapping("/function-types")
    public Result<List<String>> getFunctionTypes() {
        List<String> types = aiCallRecordService.getFunctionTypes();
        return Result.success(types);
    }
}
