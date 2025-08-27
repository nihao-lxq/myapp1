package com.example.myapp_hou.dto;

/**
 * 创建帖子数据传输对象
 * 用于接收创建新帖子的请求数据
 */
public class CreatePostDto {
    private String title;
    private String content;

    // 构造函数
    public CreatePostDto() {}

    public CreatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter和Setter方法
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
