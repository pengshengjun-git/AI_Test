package com.aitest.defect.mapper;

import com.aitest.defect.entity.Defect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 缺陷Mapper
 */
@Mapper
public interface DefectMapper extends BaseMapper<Defect> {
}
