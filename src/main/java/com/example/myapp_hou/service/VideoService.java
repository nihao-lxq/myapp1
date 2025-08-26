package com.example.myapp_hou.service;

import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.entity.Video;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * 视频服务接口
 */
public interface VideoService {
    // 上传视频
    Video uploadVideo(User user, MultipartFile file, String title, String description) throws IOException;

    // 获取用户的所有视频
    List<Video> getUserVideos(Long userId);

    // 删除视频
    void deleteVideo(Long videoId, String username) throws IOException;

    // 获取单个视频
    Video getVideoById(Long videoId);



}