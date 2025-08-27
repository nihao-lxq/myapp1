package com.example.myapp_hou.service;

import com.example.myapp_hou.dto.CommentDto;
import com.example.myapp_hou.dto.CreateCommentDto;
import com.example.myapp_hou.dto.CreatePostDto;
import com.example.myapp_hou.dto.PostDto;
import com.example.myapp_hou.entity.User;

import java.util.List;

/**
 * 帖子服务接口
 * 定义论坛模块的业务逻辑方法
 */
public interface PostService {
    /**
     * 创建新帖子
     * @param createPostDto 帖子创建信息
     * @param author 帖子作者
     * @return 创建的帖子
     */
    PostDto createPost(CreatePostDto createPostDto, User author);
    
    /**
     * 获取所有帖子
     * @return 帖子列表
     */
    List<PostDto> getAllPosts();
    
    /**
     * 根据ID获取帖子
     * @param id 帖子ID
     * @return 帖子信息
     */
    PostDto getPostById(Long id);
    
    /**
     * 根据作者获取帖子
     * @param author 帖子作者
     * @return 帖子列表
     */
    List<PostDto> getPostsByAuthor(User author);
    
    /**
     * 删除帖子
     * @param id 帖子ID
     * @param currentUser 当前用户
     */
    void deletePost(Long id, User currentUser);
    
    /**
     * 为帖子添加评论
     * @param createCommentDto 评论创建信息
     * @param author 评论作者
     * @return 创建的评论
     */
    CommentDto addCommentToPost(CreateCommentDto createCommentDto, User author);
}
