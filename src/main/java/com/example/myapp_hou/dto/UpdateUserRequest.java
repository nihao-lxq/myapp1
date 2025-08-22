package com.example.myapp_hou.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String currentPassword;  // 当前密码（用于验证）
    private String newUsername;      // 新用户名（可选）
    private String newPassword;      // 新密码（可选）
}