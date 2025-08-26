package com.example.myapp_hou.service.impl;

import com.example.myapp_hou.repository.UserRepository;
import com.example.myapp_hou.repository.VideoRepository;
import com.example.myapp_hou.service.MediaService;
import com.example.myapp_hou.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MediaServiceImpl implements MediaService {

    @Value("${file.avatar_upload.path}")
    private String avatarUploadPath;

    @Value("${file.video_upload.path}")
    private String videoUploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;
    /**
     * 存储用户头像
     */
    @Override
    public String storeAvatar(MultipartFile file, String username) throws IOException {
        return storeFile(file, username, avatarUploadPath, "avatar");
    }

    /**
     * 存储用户视频
     */
    @Override
    public String storeVideo(MultipartFile file, String username) throws IOException {
        return storeFile(file, username, videoUploadPath, "video");
    }

    /**
     *获取用户头像
     */
    @Override
    public Resource getAvatarResource(String filename) throws IOException {
        return getFileResource(filename, avatarUploadPath);
    }

    /**
     * 获取用户视频
     */
    @Override
    public Resource getVideoResource(String filename) throws IOException {
        return getFileResource(filename, videoUploadPath);
    }


    /**
     * 获取文件资源
     */
    @Override
    public void deleteAvatar(String filename) throws IOException {
        deleteFile(filename, avatarUploadPath);
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteVideo(String filename) throws IOException {
        deleteFile(filename, videoUploadPath);
    }

    /**
     *判断文件是否存在
     */
    @Override
    public boolean avatarExists(String filename) {
        return fileExists(filename, avatarUploadPath);
    }

    /**
     * 判断文件是否存在
     */
    @Override
    public boolean videoExists(String filename) {
        return fileExists(filename, videoUploadPath);
    }

    /**
     * 获取文件内容类型
     * .jpg类型.....
     */
    @Override
    public String getAvatarContentType(String filename) {
        return FileUtils.getContentType(filename);
    }

    /**
     *获取视频类型
     * .mp4类型.....
     */

    @Override
    public String getVideoContentType(String filename) {
        return FileUtils.getContentType(filename);
    }

    /**
     * 获取文件名
     * ./avatar/2023/07/05/username.jpg
     * ./video/2023/07/05/username.mp4
     */
    @Override
    public String generateFilename(String username, String originalFilename, String prefix) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成文件名: 用户ID_当前时间戳_原始文件名
        return username + "_" + System.currentTimeMillis() + "_" + prefix + fileExtension;
    }

    /**
     * 获取文件存储路径
     */
    // 私有辅助方法
    private String storeFile(MultipartFile file, String username, String uploadPath, String fileType) throws IOException {
       // 创建文件目录
        Path uploadDir = Paths.get(uploadPath);
        //判断文件目录是否存在
        if (!Files.exists(uploadDir)) {
            //创建
            Files.createDirectories(uploadDir);
        }
        //生成唯一文件名
        String filename = generateFilename(username, file.getOriginalFilename(), fileType);
        //保存文件
        Path filePath = uploadDir.resolve(filename);
        //参数一：文件输入流
        //参数二：文件保存路径
        // Files.copy：拷贝文件到指定路径（filePath）
        Files.copy(file.getInputStream(), filePath);
        System.out.println("File saved at: " + filePath.toString());
        return filename;
    }

    /**
     *获取文件资源
     */
    private Resource getFileResource(String filename, String uploadPath) throws IOException {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        }
        throw new IOException("文件不存在或不可读: " + filename);
    }

    /**
     * 删除文件
     */
    private void deleteFile(String filename, String uploadPath) throws IOException {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    /**
     * 判断文件是否存在
     * 参数一：文件名
     * 参数二：上传目录
     */
    private boolean fileExists(String filename, String uploadPath) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("文件名为空");
            return false;
        }
        Path filePath = Paths.get(uploadPath).resolve(filename);
        System.out.println("🔍 检查文件路径: " + filePath.toAbsolutePath());
        System.out.println("📁 上传目录: " + videoUploadPath);

        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
}