package com.example.myapp_hou.dto;

import com.example.myapp_hou.entity.Video;
import lombok.Data;

@Data
public class VideoDto {
    private Long id;
    private String filename;
    private String title;
    private String description;
    private Long fileSize;
    private String createdAt;
    private String videoUrl;

    public static VideoDto from(Video video) {
        VideoDto dto = new VideoDto();
        dto.setId(video.getId());
        dto.setFilename(video.getFilename());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setFileSize(video.getFileSize());

        //  安全处理 null
        if (video.getCreatedAt() != null) {
            dto.setCreatedAt(video.getCreatedAt().toString());
        } else {
            dto.setCreatedAt(null); // 或设置默认值，如 "Unknown"
        }
        dto.setVideoUrl("/api/videos/stream" + video.getFilename());
        return dto;
    }
}