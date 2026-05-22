package com.aitest.user.service;

import com.aitest.user.dto.AiCallRecordQueryDTO;
import com.aitest.user.dto.AiCallRecordVO;
import com.aitest.user.dto.AiStatisticsVO;
import com.aitest.user.entity.AiCallRecord;
import com.aitest.user.entity.User;
import com.aitest.user.mapper.AiCallRecordMapper;
import com.aitest.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI调用记录服务
 */
@Service
public class AiCallRecordService extends ServiceImpl<AiCallRecordMapper, AiCallRecord> {

    private final AiCallRecordMapper aiCallRecordMapper;
    private final UserMapper userMapper;

    public AiCallRecordService(AiCallRecordMapper aiCallRecordMapper, UserMapper userMapper) {
        this.aiCallRecordMapper = aiCallRecordMapper;
        this.userMapper = userMapper;
    }

    /**
     * 分页查询调用记录
     */
    public IPage<AiCallRecordVO> queryRecords(AiCallRecordQueryDTO dto) {
        Page<AiCallRecord> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<AiCallRecord> wrapper = buildQueryWrapper(dto);
        
        IPage<AiCallRecord> resultPage = page(page, wrapper);
        
        return resultPage.convert(this::convertToVO);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<AiCallRecord> buildQueryWrapper(AiCallRecordQueryDTO dto) {
        LambdaQueryWrapper<AiCallRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (dto.getUserId() != null) {
            wrapper.eq(AiCallRecord::getUserId, dto.getUserId());
        }
        if (dto.getProjectId() != null) {
            wrapper.eq(AiCallRecord::getProjectId, dto.getProjectId());
        }
        if (dto.getFunctionType() != null && !dto.getFunctionType().isEmpty()) {
            wrapper.eq(AiCallRecord::getFunctionType, dto.getFunctionType());
        }
        if (dto.getModelName() != null && !dto.getModelName().isEmpty()) {
            wrapper.eq(AiCallRecord::getModelName, dto.getModelName());
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            wrapper.eq(AiCallRecord::getStatus, dto.getStatus());
        }
        if (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(dto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            wrapper.ge(AiCallRecord::getCreatedAt, start);
        }
        if (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) {
            LocalDateTime end = LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            wrapper.le(AiCallRecord::getCreatedAt, end);
        }
        
        wrapper.orderByDesc(AiCallRecord::getCreatedAt);
        return wrapper;
    }

    /**
     * 获取统计数据
     */
    public AiStatisticsVO getStatistics(Long userId, String startTime, String endTime) {
        AiStatisticsVO statistics = new AiStatisticsVO();
        
        // 总调用次数
        Long totalCalls = aiCallRecordMapper.countRecords(userId, startTime, endTime);
        statistics.setTotalCalls(totalCalls);
        
        // 总费用
        BigDecimal totalCost = aiCallRecordMapper.sumCost(userId, startTime, endTime);
        statistics.setTotalCost(totalCost != null ? totalCost.doubleValue() : 0.0);
        
        // 总Token数
        Integer totalTokens = aiCallRecordMapper.sumTokens(userId, startTime, endTime);
        statistics.setTotalTokens(totalTokens != null ? totalTokens : 0);
        
        // 成功率
        Long successCount = aiCallRecordMapper.countSuccessRecords(userId, startTime, endTime);
        double successRate = totalCalls > 0 ? 
                BigDecimal.valueOf(successCount * 100.0 / totalCalls)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue() : 0;
        statistics.setSuccessRate(successRate);
        
        // 每日统计
        List<Map<String, Object>> dailyData = aiCallRecordMapper.getDailyStatistics(userId, startTime, endTime);
        statistics.setDailyStats(dailyData.stream().map(this::mapToDailyStats).collect(Collectors.toList()));
        
        // 模型统计
        List<Map<String, Object>> modelData = aiCallRecordMapper.getModelStatistics(userId, startTime, endTime);
        statistics.setModelStats(modelData.stream().map(this::mapToModelStats).collect(Collectors.toList()));
        
        // 功能统计
        List<Map<String, Object>> functionData = aiCallRecordMapper.getFunctionStatistics(userId, startTime, endTime);
        statistics.setFunctionStats(functionData.stream().map(this::mapToFunctionStats).collect(Collectors.toList()));
        
        // 用户统计（仅限管理员）
        if (userId == null) {
            List<Map<String, Object>> userData = aiCallRecordMapper.getUserStatistics(startTime, endTime);
            statistics.setUserStats(userData.stream().map(this::mapToUserStats).collect(Collectors.toList()));
        }
        
        return statistics;
    }

    /**
     * 获取模型列表
     */
    public List<String> getModelNames() {
        return aiCallRecordMapper.selectDistinctModelNames();
    }

    /**
     * 获取功能类型列表
     */
    public List<String> getFunctionTypes() {
        return aiCallRecordMapper.selectDistinctFunctionTypes();
    }

    /**
     * 转换为VO
     */
    private AiCallRecordVO convertToVO(AiCallRecord record) {
        AiCallRecordVO vo = new AiCallRecordVO();
        BeanUtils.copyProperties(record, vo);
        
        // 设置用户名
        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        
        return vo;
    }

    private AiStatisticsVO.DailyStats mapToDailyStats(Map<String, Object> map) {
        AiStatisticsVO.DailyStats stats = new AiStatisticsVO.DailyStats();
        stats.setDate((String) map.get("date"));
        stats.setCalls(((Number) map.get("calls")).longValue());
        stats.setCost(map.get("cost") != null ? ((BigDecimal) map.get("cost")).doubleValue() : 0);
        stats.setTokens(map.get("tokens") != null ? ((Number) map.get("tokens")).intValue() : 0);
        return stats;
    }

    private AiStatisticsVO.ModelStats mapToModelStats(Map<String, Object> map) {
        AiStatisticsVO.ModelStats stats = new AiStatisticsVO.ModelStats();
        stats.setModelName((String) map.get("model_name"));
        stats.setCalls(((Number) map.get("calls")).longValue());
        stats.setCost(map.get("cost") != null ? ((BigDecimal) map.get("cost")).doubleValue() : 0);
        stats.setTokens(map.get("tokens") != null ? ((Number) map.get("tokens")).intValue() : 0);
        return stats;
    }

    private AiStatisticsVO.FunctionStats mapToFunctionStats(Map<String, Object> map) {
        AiStatisticsVO.FunctionStats stats = new AiStatisticsVO.FunctionStats();
        stats.setFunctionType((String) map.get("function_type"));
        stats.setCalls(((Number) map.get("calls")).longValue());
        stats.setCost(map.get("cost") != null ? ((BigDecimal) map.get("cost")).doubleValue() : 0);
        stats.setTokens(map.get("tokens") != null ? ((Number) map.get("tokens")).intValue() : 0);
        return stats;
    }

    private AiStatisticsVO.UserStats mapToUserStats(Map<String, Object> map) {
        AiStatisticsVO.UserStats stats = new AiStatisticsVO.UserStats();
        stats.setUsername((String) map.get("username"));
        stats.setCalls(((Number) map.get("calls")).longValue());
        stats.setCost(map.get("cost") != null ? ((BigDecimal) map.get("cost")).doubleValue() : 0);
        stats.setTokens(map.get("tokens") != null ? ((Number) map.get("tokens")).intValue() : 0);
        return stats;
    }
}
