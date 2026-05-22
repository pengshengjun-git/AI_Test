package com.aitest.user.dto;

import lombok.Data;

/**
 * AI调用记录查询条件DTO
 */
@Data
public class AiCallRecordQueryDTO {

    private Long userId;
    private Long projectId;
    private String functionType;
    private String modelName;
    private String status;
    private String startTime;
    private String endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
