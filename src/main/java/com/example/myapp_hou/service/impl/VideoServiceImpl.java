package com.example.myapp_hou.service.impl;

import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.entity.Video;
import com.example.myapp_hou.repository.VideoRepository;
import com.example.myapp_hou.service.MediaService;
import com.example.myapp_hou.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  视频服务实现，处理视频上传、查询、删除等业务逻辑
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

     @Autowired
     private MediaService mediaService;

    /**
    * 上传视频
     */
    @Override
    public Video uploadVideo(User user, MultipartFile file, String title, String description) throws IOException {
        // 保存文件
        String filename = mediaService.storeVideo(file, user.getUsername());
        System.out.println("文件保存成功: " + filename);
        // 创建视频记录
        Video video = new Video();
        video.setFilename(filename);
        video.setTitle(title != null ? title : file.getOriginalFilename());
        video.setDescription(description);
        video.setFileSize(file.getSize());
        video.setUser(user);
        video.setCreatedAt(LocalDateTime.now()); // 明确设置创建时间


        // 保存视频记录

        return videoRepository.save(video);

    }

    /**
     * 获取用户视频
     * 根据用户id获取用户所有视频
     * @param userId：用户id
     * @return
     */
    @Override
    public List<Video> getUserVideos(Long userId) {
        return videoRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 删除某个视频
     * @param videoId
     * @param username
     * @throws IOException
     */
    @Override
    public void deleteVideo(Long videoId, String username) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));

        // 验证权限：只能删除自己的视频
        // 这里假设用户名是唯一的
        if (!video.getUser().getUsername().equals(username)) {
            throw new RuntimeException("无权删除此视频");
        }

        // 删除物理文件
        mediaService.deleteVideo(video.getFilename());

        // 删除数据库记录
        videoRepository.delete(video);
    }

    /**
     * 获取某个视频
     * @param videoId
     * @return
     */
    @Override
    public Video getVideoById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
    }
}