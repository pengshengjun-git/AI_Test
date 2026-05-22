package com.aitest.dashboard.dto;

import lombok.Data;

@Data
public class TestPlanQueryDTO {
    private String name;
    private String status;
    private Long projectId;
    private Integer page = 1;
    private Integer size = 10;
}
