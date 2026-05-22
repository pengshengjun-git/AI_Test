package com.aitest.user.dto;

import lombok.Data;

/**
 * 菜单创建请求DTO
 */
@Data
public class MenuCreateDTO {

    private Long parentId;
    private String name;
    private String code;
    private String path;
    private String component;
    private String icon;
    private String type;
    private String permission;
    private Integer sortOrder;
    private Integer visible;
    private Integer cacheable;
    private String redirect;
    private String description;
}
