package com.aitest.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private Long userId;
    private String realName;
    private String email;
    private String role;
    private List<String> roles;
    private List<String> permissions;
    private Long departmentId;
}