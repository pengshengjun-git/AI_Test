package com.aitest.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CoverageCreateDTO {

    @NotNull(message = "项目ID不能为空")
    @JsonProperty("project_id")
    private Long projectId;

    @JsonProperty("covered_lines")
    private Long coveredLines;

    @JsonProperty("total_lines")
    private Long totalLines;

    @JsonProperty("coverage_rate")
    private Double coverageRate;

    @JsonProperty("report_date")
    private LocalDate reportDate;
}
