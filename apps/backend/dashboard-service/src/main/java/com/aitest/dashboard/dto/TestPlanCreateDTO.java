package com.aitest.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestPlanCreateDTO {

    @NotBlank(message = "名称不能为空")
    private String name;

    private String description;

    @JsonProperty("project_id")
    private Long projectId;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    private LocalDateTime endDate;

    private String status;

    private String owner;
}
