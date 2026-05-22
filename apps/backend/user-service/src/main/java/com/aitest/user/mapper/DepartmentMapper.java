package com.aitest.user.mapper;

import com.aitest.user.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门Mapper
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
