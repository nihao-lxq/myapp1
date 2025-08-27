package com.example.myapp_hou.controller;

import com.example.myapp_hou.dto.CommentDto;
import com.example.myapp_hou.dto.CreateCommentDto;
import com.example.myapp_hou.dto.CreatePostDto;
import com.example.myapp_hou.dto.PostDto;
import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.service.PostService;
import com.example.myapp_hou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论坛控制器
 * 处理论坛相关的HTTP请求
 */
@RestController
@RequestMapping("/api/forum")
@CrossOrigin(origins = "*")
public class ForumController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * 创建新帖子
     *
     * @param createPostDto 帖子创建信息
     * @return 创建的帖子
     */
    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto createPostDto) {
        // 获取当前认证用户
        User currentUser = getCurrentUser();
        // 创建帖子
        PostDto postDto = postService.createPost(createPostDto, currentUser);
        return ResponseEntity.ok(postDto);
    }

    /**
     * 获取所有帖子
     *
     * @return 帖子列表
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        // 获取所有帖子
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * 根据ID获取帖子
     *
     * @param id 帖子ID
     * @return 帖子信息
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        // 根据ID获取帖子
        PostDto postDto = postService.getPostById(id);
        if (postDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postDto);
    }

    /**
     * 删除帖子
     *
     * @param id 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        // 获取当前认证用户
        User currentUser = getCurrentUser();
        try {
            // 删除帖子
            postService.deletePost(id, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 添加评论
     *
     * @param createCommentDto 评论创建信息
     * @return 创建的评论
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDto> addComment(@RequestBody CreateCommentDto createCommentDto) {
        // 获取当前认证用户
        User currentUser = getCurrentUser();
        // 添加评论
        CommentDto commentDto = postService.addCommentToPost(createCommentDto, currentUser);
        return ResponseEntity.ok(commentDto);
    }

    /**
     * 获取当前认证用户
     *
     * @return 当前用户
     */
    private User getCurrentUser() {
        // 从安全上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // 根据用户名查询用户
        return userService.findByUsername(username);
    }
}
