package com.aitest.dashboard.dto;

import lombok.Data;

@Data
public class CoverageQueryDTO {
    private Long projectId;
    private Integer page = 1;
    private Integer size = 10;
}
