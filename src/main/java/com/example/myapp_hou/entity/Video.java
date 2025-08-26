package com.example.myapp_hou.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * @Entity:表示这个 Java 类对应数据库中的一张表,JPA（Java Persistence API）会自动把它映射成一张表
 * @Table(name = "videos") —— 指定表名
 *视频实体类，包含视频的基本信息和与用户的关联关系
 *
 */

// Video.java - 新的视频表
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动增长
    private Long id;

//JPA 默认会把 fileSize → file_size，createdAt → created_at
    private String filename;    // 视频文件名
    private String title;       // 视频标题
    private String description; // 视频描述
    private Long fileSize;      // 文件大小
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 上传时间
//    @PrePersist     //在插入数据时执行
//    protected void onCreate() {
//        if (createdAt == null) {
//            createdAt = LocalDateTime.now();
//        }
//    }
    //外键关系
    // 多对一关系：多个视频属于一个用户
    //@JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)//懒加载
    @JoinColumn(name = "user_id")//关联外键
    private User user;  // 视频所属的用户
//
//    // 构造函数、getter、setter
//    public Video() {}
//
//    public Video(String filename, String title, User user) {
//        this.filename = filename;
//        this.title = title;
//        this.user = user;
//        this.createdAt = LocalDateTime.now();
//    }
}