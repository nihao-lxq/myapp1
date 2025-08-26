package com.example.myapp_hou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 让上传的文件（如头像、视频）可以通过浏览器直接访问
 * 用 @GetMapping 就够了，不需要 WebConfig
 * 先匹配Controller：/api/media/video/test.mp4 → 找到@GetMapping
 *
 * 如果没有Controller匹配：/videos/test.mp4 → 交给ResourceHandler
 *
 * 如果ResourceHandler也没匹配：返回404
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${file.video_upload.path}")
    private String videoUploadPath;
    
    @Value("${file.avatar_upload.path}")
    private String avatarUploadPath;

    /**
     *浏览器来访问的路径
     * addResourceHandler 是“对外”的，根据前端怎么用、项目规范来设计
     * addResourceHandlers：把服务器上的某个文件夹，映射成一个可以通过 URL 访问的路径。
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射头像文件
        registry.addResourceHandler("/api/avatar/**")
                .addResourceLocations("file:" + avatarUploadPath + "/");
//
//        // 映射视频文件
//        registry.addResourceHandler("/api/videos/stream/**")
//                .addResourceLocations("file:" + videoUploadPath + "/");
    }
}