package com.aitest.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI调用记录实体
 */
@Data
@TableName("ai_call_record")
public class AiCallRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 功能类型
     */
    private String functionType;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * Prompt Token数
     */
    private Integer promptTokens;

    /**
     * Completion Token数
     */
    private Integer completionTokens;

    /**
     * 总Token数
     */
    private Integer totalTokens;

    /**
     * 状态: success-成功, failed-失败
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 响应时间(毫秒)
     */
    private Long responseTime;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 输入数据JSON
     */
    private String inputData;

    /**
     * 输出数据JSON
     */
    private String outputData;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
