package com.aitest.defect.service;

import com.aitest.defect.dto.DefectCreateDTO;
import com.aitest.defect.dto.DefectQueryDTO;
import com.aitest.defect.entity.Defect;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 缺陷服务接口
 */
public interface DefectService {

    Defect createDefect(DefectCreateDTO dto);

    Defect updateDefect(Long id, DefectCreateDTO dto);

    boolean deleteDefect(Long id);

    boolean batchDeleteDefects(List<Long> ids);

    Defect getDefectById(Long id);

    IPage<Defect> queryDefects(DefectQueryDTO dto);

    List<Defect> getDefectsByProjectId(Long projectId);

    Defect assignDefect(Long id, Long assignee);
}
