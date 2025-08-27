package com.example.myapp_hou.dto;

/**
 * 创建评论数据传输对象
 * 用于接收创建新评论的请求数据
 */
public class CreateCommentDto {
    private String content;
    private Long postId;

    // 构造函数
    public CreateCommentDto() {}

    public CreateCommentDto(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }

    // Getter和Setter方法
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
