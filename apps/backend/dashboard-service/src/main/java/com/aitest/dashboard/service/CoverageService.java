package com.aitest.dashboard.service;

import com.aitest.dashboard.dto.CoverageCreateDTO;
import com.aitest.dashboard.dto.CoverageQueryDTO;
import com.aitest.dashboard.entity.Coverage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

public interface CoverageService {
    IPage<Coverage> queryCoverages(CoverageQueryDTO dto);
    Coverage getCoverageById(Long id);
    Coverage createCoverage(CoverageCreateDTO dto);
    Coverage updateCoverage(Long id, CoverageCreateDTO dto);
    boolean deleteCoverage(Long id);
    List<Coverage> getCoveragesByProjectId(Long projectId);
    Map<String, Object> getCoverageStats();
}
