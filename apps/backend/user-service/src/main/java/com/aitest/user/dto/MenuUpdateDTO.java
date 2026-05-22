package com.aitest.user.dto;

import lombok.Data;

/**
 * 菜单更新请求DTO
 */
@Data
public class MenuUpdateDTO {

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
