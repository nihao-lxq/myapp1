package com.example.myapp_hou.service;

import com.example.myapp_hou.dto.RegisterRequest;
import com.example.myapp_hou.dto.UpdateUserRequest;
import com.example.myapp_hou.dto.UserResponse;
import com.example.myapp_hou.entity.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 处理用户注册
 * 用户登录由Spring Security的认证流程处理
 */
public interface UserService {
    //参数: RegisterRequest（前端传来的注册数据）
    UserResponse register(RegisterRequest request);
    boolean existsByUsername(String username);
    //参数一：当前用户名
    //参数二：更新用户信息（前端传来的更新数据）
    UserResponse updateUser(String currentUsername, UpdateUserRequest request);
    //更新（上传）头像
    UserResponse updateAvatar(String currentUsername, MultipartFile avatar) throws IOException;

    // 获取头像文件名（用于拼接 URL）
    String getAvatarUrl(Long userId);

    @Transactional(readOnly = true)
    public UserResponse getUserResponse(User user) ;
}
