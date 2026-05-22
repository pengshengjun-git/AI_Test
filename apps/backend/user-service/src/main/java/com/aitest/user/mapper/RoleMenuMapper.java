package com.aitest.user.mapper;

import com.aitest.user.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 批量插入角色菜单关联
     */
    int batchInsert(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 根据角色ID删除
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
