package com.example.myapp_hou.service;

import com.example.myapp_hou.entity.Video;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// service/MediaService.java
public interface MediaService {
    // 统一的文件操作方法
    String storeAvatar(MultipartFile file, String username) throws IOException;
    String storeVideo(MultipartFile file, String username) throws IOException;
    void deleteAvatar(String filename) throws IOException;
    void deleteVideo(String filename) throws IOException;

//    // 统一的资源访问方法
    Resource getAvatarResource(String filename) throws IOException;
   Resource getVideoResource(String filename) throws IOException;
    String getVideoContentType(String filename);
    String getAvatarContentType(String filename);

    // 统一的检查方法
    boolean avatarExists(String filename);
    boolean videoExists(String filename);

    //生成文件名
    String generateFilename(String username, String originalFilename, String prefix);

}