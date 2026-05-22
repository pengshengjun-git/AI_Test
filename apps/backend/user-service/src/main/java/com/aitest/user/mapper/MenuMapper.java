package com.aitest.user.mapper;

import com.aitest.user.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色ID列表查询菜单
     */
    @Select("<script>" +
            "SELECT DISTINCT m.* FROM menu m " +
            "INNER JOIN role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id IN " +
            "<foreach collection='roleIds' item='roleId' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach>" +
            "AND m.deleted = 0 AND m.visible = 1 " +
            "ORDER BY m.parent_id, m.sort_order" +
            "</script>")
    List<Menu> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户ID查询菜单
     */
    @Select("<script>" +
            "SELECT DISTINCT m.* FROM menu m " +
            "INNER JOIN role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "AND m.deleted = 0 AND m.visible = 1 " +
            "ORDER BY m.parent_id, m.sort_order" +
            "</script>")
    List<Menu> selectByUserId(@Param("userId") Long userId);
}
