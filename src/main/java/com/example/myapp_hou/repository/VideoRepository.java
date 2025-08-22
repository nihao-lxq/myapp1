package com.example.myapp_hou.repository;

import com.example.myapp_hou.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
/**
 * 视频数据访问接口，提供按用户ID查询等方法
 */
public interface VideoRepository extends JpaRepository<Video, Long> {
    // 根据用户ID查找视频
    Optional<Video> findById(Long videoId);

    // 根据用户ID查找视频（按时间倒序）
    List<Video> findByUserIdOrderByCreatedAtDesc(Long userId);

}