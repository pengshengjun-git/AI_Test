package com.aitest.user.dto;

import lombok.Data;

/**
 * AI调用记录VO
 */
@Data
public class AiCallRecordVO {

    private Long id;
    private Long userId;
    private String username;
    private Long projectId;
    private String projectName;
    private String functionType;
    private String modelName;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
    private String status;
    private String errorMessage;
    private Long responseTime;
    private String requestId;
    private Double cost;
    private String ipAddress;
    private java.time.LocalDateTime createdAt;
}
