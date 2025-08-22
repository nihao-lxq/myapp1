package com.example.myapp_hou.controller;



import com.example.myapp_hou.dto.UserResponse;
import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.service.MediaService;
import com.example.myapp_hou.service.UserService;
import com.example.myapp_hou.service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/avatar")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})

public class AvatarController {
    @Autowired
    private UserService userService;

//    @Autowired
//    private AvatarStorageService avatarStorageService;


    @Autowired
    private MediaService mediaService;

    /**
     * 上传用户头像
     * @RequestParam("avatar")会将前端传过来的文件传进 MultipartFile file,
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>>uploadAvatar
    (@RequestParam("avatar") MultipartFile file, // 接收上传的文件
     Authentication authentication)       // 获取当前用户
    {
        try {
            String userName = authentication.getName();
            //得到用户头像的URL
            UserResponse user = userService.updateAvatar(userName, file); // 调用服务层
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "头像上传成功");
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message","文件上传失败" +e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

//    /**
//     * 展示头像
//     * 获取用户头像URL
//     */
//    @GetMapping("/{userId}")
//    public ResponseEntity<Map<String, Object>> getAvatarUrl(@PathVariable Long userId) {
//        try {
//            // 调用用户服务获取头像URL
//            String avatarUrl = userService.getAvatarUrl(userId);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("avatarUrl", avatarUrl);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> error = new HashMap<>();
//            error.put("success", false);
//            error.put("message", "获取头像失败: " + e.getMessage());
//            return ResponseEntity.badRequest().body(error);
//        }
//    }

    /**
     * 直接返回图片流（供<img>标签使用）
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Resource> getAvatarImage(@PathVariable Long userId) {
        try {
            String filename = userService.getAvatarUrl(userId);
            if (filename == null) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = mediaService.getAvatarResource(filename);
            String contentType = mediaService.getAvatarContentType(filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
