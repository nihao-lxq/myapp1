package com.example.myapp_hou.dto;

import lombok.Data;

/**
 * 接收注册请求数据
 * @RequestBody 这个会将前端的发过来的数据传进RegisterRequest request
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
}
