package com.example.myapp_hou.controller;

import com.example.myapp_hou.dto.VideoDto;
import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.entity.Video;
import com.example.myapp_hou.service.MediaService;
import com.example.myapp_hou.service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private MediaService mediaService;

    @Value("${file.video_upload.path}")
    private String videoUploadPath;

    // 上传视频
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadVideo(
            @RequestParam("video") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication) {
        
        try {

            Video video = videoService.uploadVideo(
                (User) authentication.getPrincipal(), //获取当前用户
                file, title, description
            );
            VideoDto videoDto = VideoDto.from(video); // 转成 DTO
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "视频上传成功");
            response.put("video", videoDto);

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "视频上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 获取用户的所有视频
    @GetMapping("/myAllVideos")
    public ResponseEntity<Map<String, Object>> getMyVideos(Authentication authentication) {
      // 获取当前用户
        User user = (User) authentication.getPrincipal();
        // 获取当前用户的视频
        List<Video> videos = videoService.getUserVideos(user.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("videos", videos);
        return ResponseEntity.ok(response);
    }

    // 删除视频
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Map<String, Object>> deleteVideo(
            @PathVariable Long videoId,
            Authentication authentication) {
        
        try {
            // 删除视频
            videoService.deleteVideo(videoId, authentication.getName());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "视频删除成功");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "视频删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //获取某个视频
    @GetMapping("/{videoId}")
    public ResponseEntity<Map<String, Object>> getVideoById(@PathVariable Long videoId) {
        try {
            Video video = videoService.getVideoById(videoId);
            VideoDto videoDto = VideoDto.from(video); // 转成 DTO
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("video", videoDto);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取视频失败");
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 流式传输视频文件
     */
    @GetMapping("/stream/{filename:.+}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String filename) {
        try {
//            // 获取视频信息
//            var video = videoService.getVideoById(videoId);
//            String filename = video.getFilename();
//
//
//            // 添加调试日志
//            System.out.println("Looking for video file: " + filename);
//            //System.out.println("Video path: " + videoUploadPath);
            System.out.println("🎬 请求播放视频: " + filename);
            if(!mediaService.videoExists(filename))
            {
                System.out.println("❌ 视频文件不存在: " + filename);
                return ResponseEntity.notFound().build();
            }


            Resource resource = (Resource) mediaService.getVideoResource(filename);
            String contentType = mediaService.getVideoContentType(filename);

            // 设置支持视频流的头信息
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);

        } catch (Exception e) {
            System.out.println("❌ 播放视频异常: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 测试文件是否存在
     * @param filename
     * @return
     */
    @GetMapping("/debug/file/{filename:.+}")
    public ResponseEntity<Map<String, Object>> debugFile(@PathVariable String filename) {
        Map<String, Object> response = new HashMap<>();

        try {
            Path filePath = Paths.get(videoUploadPath).resolve(filename);
            boolean exists = Files.exists(filePath);
            boolean isFile = exists && Files.isRegularFile(filePath);
            boolean readable = isFile && Files.isReadable(filePath);

            response.put("filename", filename);
            response.put("filePath", filePath.toAbsolutePath().toString());
            response.put("exists", exists);
            response.put("isFile", isFile);
            response.put("readable", readable);

            if (exists && isFile) {
                response.put("fileSize", Files.size(filePath));
            }

            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}