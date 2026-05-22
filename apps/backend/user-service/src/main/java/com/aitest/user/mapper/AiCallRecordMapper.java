package com.aitest.user.mapper;

import com.aitest.user.entity.AiCallRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AI调用记录Mapper
 */
@Mapper
public interface AiCallRecordMapper extends BaseMapper<AiCallRecord> {

    /**
     * 统计调用次数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='projectId != null'>AND project_id = #{projectId}</if>" +
            "<if test='startTime != null'>AND created_at &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND created_at &lt;= #{endTime}</if>" +
            "</script>")
    Long countCallRecords(@Param("userId") Long userId,
                           @Param("projectId") Long projectId,
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime);

    /**
     * 统计总Token数
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(total_tokens), 0) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='projectId != null'>AND project_id = #{projectId}</if>" +
            "<if test='startTime != null'>AND created_at &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND created_at &lt;= #{endTime}</if>" +
            "</script>")
    Long sumTotalTokens(@Param("userId") Long userId,
                        @Param("projectId") Long projectId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

    /**
     * 统计总成本
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(cost), 0) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='projectId != null'>AND project_id = #{projectId}</if>" +
            "<if test='startTime != null'>AND created_at &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND created_at &lt;= #{endTime}</if>" +
            "</script>")
    BigDecimal sumCost(@Param("userId") Long userId,
                       @Param("projectId") Long projectId,
                       @Param("startTime") LocalDateTime startTime,
                       @Param("endTime") LocalDateTime endTime);

    /**
     * 统计成功率
     */
    @Select("<script>" +
            "SELECT " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN status = 'success' THEN 1 ELSE 0 END) as success " +
            "FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='projectId != null'>AND project_id = #{projectId}</if>" +
            "<if test='startTime != null'>AND created_at &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND created_at &lt;= #{endTime}</if>" +
            "</script>")
    Map<String, Object> statisticsSuccessRate(@Param("userId") Long userId,
                                               @Param("projectId") Long projectId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 统计调用次数（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "</script>")
    Long countRecords(@Param("userId") Long userId,
                      @Param("startTime") String startTime,
                      @Param("endTime") String endTime);

    /**
     * 统计总Token数（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(input_tokens + output_tokens), 0) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "</script>")
    Integer sumTokens(@Param("userId") Long userId,
                      @Param("startTime") String startTime,
                      @Param("endTime") String endTime);

    /**
     * 统计成功次数（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM ai_call_record " +
            "WHERE status = 1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "</script>")
    Long countSuccessRecords(@Param("userId") Long userId,
                             @Param("startTime") String startTime,
                             @Param("endTime") String endTime);

    /**
     * 统计总成本（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(cost), 0) FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "</script>")
    BigDecimal sumCost(@Param("userId") Long userId,
                       @Param("startTime") String startTime,
                       @Param("endTime") String endTime);

    /**
     * 每日统计（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT DATE(request_time) as date, COUNT(*) as count " +
            "FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "GROUP BY DATE(request_time) ORDER BY date DESC" +
            "</script>")
    List<Map<String, Object>> getDailyStatistics(@Param("userId") Long userId,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);

    /**
     * 模型统计（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT model_name as modelName, COUNT(*) as count, " +
            "SUM(input_tokens) as inputTokens, SUM(output_tokens) as outputTokens, SUM(cost) as cost " +
            "FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "GROUP BY model_name ORDER BY count DESC" +
            "</script>")
    List<Map<String, Object>> getModelStatistics(@Param("userId") Long userId,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);

    /**
     * 功能统计（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT function_type as functionType, COUNT(*) as count " +
            "FROM ai_call_record " +
            "WHERE function_type IS NOT NULL " +
            "<if test='userId != null'>AND user_id = #{userId}</if>" +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "GROUP BY function_type ORDER BY count DESC" +
            "</script>")
    List<Map<String, Object>> getFunctionStatistics(@Param("userId") Long userId,
                                                     @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime);

    /**
     * 用户统计（字符串日期参数）
     */
    @Select("<script>" +
            "SELECT user_id as userId, COUNT(*) as count, SUM(cost) as cost " +
            "FROM ai_call_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'>AND request_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'>AND request_time &lt;= #{endTime}</if>" +
            "GROUP BY user_id ORDER BY count DESC LIMIT 10" +
            "</script>")
    List<Map<String, Object>> getUserStatistics(@Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

    /**
     * 获取所有模型名称
     */
    @Select("SELECT DISTINCT model_name FROM ai_call_record WHERE model_name IS NOT NULL")
    List<String> selectDistinctModelNames();

    /**
     * 获取所有功能类型
     */
    @Select("SELECT DISTINCT function_type FROM ai_call_record WHERE function_type IS NOT NULL")
    List<String> selectDistinctFunctionTypes();
}
