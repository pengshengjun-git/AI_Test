package com.aitest.user.dto;

import lombok.Data;

import java.util.List;

/**
 * AI调用统计VO
 */
@Data
public class AiStatisticsVO {

    private Long totalCalls;
    private Double totalCost;
    private Integer totalTokens;
    private Double successRate;
    private List<DailyStats> dailyStats;
    private List<ModelStats> modelStats;
    private List<FunctionStats> functionStats;
    private List<UserStats> userStats;

    @Data
    public static class DailyStats {
        private String date;
        private Long calls;
        private Double cost;
        private Integer tokens;
    }

    @Data
    public static class ModelStats {
        private String modelName;
        private Long calls;
        private Double cost;
        private Integer tokens;
    }

    @Data
    public static class FunctionStats {
        private String functionType;
        private Long calls;
        private Double cost;
        private Integer tokens;
    }

    @Data
    public static class UserStats {
        private String username;
        private Long calls;
        private Double cost;
        private Integer tokens;
    }
}
