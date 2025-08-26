package com.example.myapp_hou.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {

    /**
     * 工具类
     * 根据文件名判断内容类型
     */
    public static String getContentType(String filename) {
        String lowerFilename = filename.toLowerCase();
        
        // 图片类型
        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (lowerFilename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else if (lowerFilename.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerFilename.endsWith(".webp")) {
            return "image/webp";
        }
        
        // 视频类型
        else if (lowerFilename.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowerFilename.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (lowerFilename.endsWith(".mov")) {
            return "video/quicktime";
        } else if (lowerFilename.endsWith(".webm")) {
            return "video/webm";
        } else if (lowerFilename.endsWith(".mkv")) {
            return "video/x-matroska";
        }
        
        // 其他类型
        else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    /**
     * 加载文件资源
     */
    public static Resource loadFileResource(Path filePath) throws IOException {
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("文件不存在或不可读: " + filePath);
        }
    }

    /**
     * 检查文件扩展名是否支持
     */
    public static boolean isSupportedImageFormat(String filename) {
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg") ||
               lowerFilename.endsWith(".png") || lowerFilename.endsWith(".gif") ||
               lowerFilename.endsWith(".bmp") || lowerFilename.endsWith(".webp");
    }

    public static boolean isSupportedVideoFormat(String filename) {
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".mp4") || lowerFilename.endsWith(".avi") ||
               lowerFilename.endsWith(".mov") || lowerFilename.endsWith(".webm") ||
               lowerFilename.endsWith(".mkv");
    }
}