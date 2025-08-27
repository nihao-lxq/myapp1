package com.example.myapp_hou.service.impl;

import com.example.myapp_hou.dto.CommentDto;
import com.example.myapp_hou.dto.CreateCommentDto;
import com.example.myapp_hou.dto.CreatePostDto;
import com.example.myapp_hou.dto.PostDto;
import com.example.myapp_hou.entity.Comment;
import com.example.myapp_hou.entity.Post;
import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.repository.CommentRepository;
import com.example.myapp_hou.repository.PostRepository;
import com.example.myapp_hou.repository.UserRepository;
import com.example.myapp_hou.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 * 实现PostService接口中定义的业务逻辑
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建新帖子
     * @param createPostDto 帖子创建信息
     * @param author 帖子作者
     * @return 创建的帖子
     */
    @Override
    public PostDto createPost(CreatePostDto createPostDto, User author) {
        // 创建帖子实体并保存到数据库
        Post post = new Post(createPostDto.getTitle(), createPostDto.getContent(), author);
        Post savedPost = postRepository.save(post);

        // 转换为DTO并返回
        return convertToPostDto(savedPost);
    }

    /**
     * 获取所有帖子
     * @return 帖子列表
     */
    @Override
    public List<PostDto> getAllPosts() {
        // 查询所有帖子并转换为DTO列表
        return postRepository.findByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取帖子
     * @param id 帖子ID
     * @return 帖子信息
     */
    @Override
    public PostDto getPostById(Long id) {
        // 根据ID查询帖子
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        // 转换为DTO并返回
        return convertToPostDto(post);
    }

    /**
     * 根据作者获取帖子
     * @param author 帖子作者
     * @return 帖子列表
     */
    @Override
    public List<PostDto> getPostsByAuthor(User author) {
        // 查询指定作者的帖子并转换为DTO列表
        return postRepository.findByAuthorOrderByCreatedAtDesc(author)
                .stream()
                .map(this::convertToPostDto)
                .collect(Collectors.toList());
    }

    /**
     * 删除帖子
     * @param id 帖子ID
     * @param currentUser 当前用户
     */
    @Override
    public void deletePost(Long id, User currentUser) {
        // 根据ID查询帖子
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        // 检查当前用户是否为帖子作者
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        // 删除帖子
        postRepository.deleteById(id);
    }

    /**
     * 为帖子添加评论
     * @param createCommentDto 评论创建信息
     * @param author 评论作者
     * @return 创建的评论
     */
    @Override
    public CommentDto addCommentToPost(CreateCommentDto createCommentDto, User author) {
        // 根据ID查询帖子
        Post post = postRepository.findById(createCommentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 创建评论实体并保存到数据库
        Comment comment = new Comment(createCommentDto.getContent(), post, author);
        Comment savedComment = commentRepository.save(comment);

        // 转换为DTO并返回
        return convertToCommentDto(savedComment);
    }

    /**
     * 将Post实体转换为PostDto
     * @param post 帖子实体
     * @return 帖子DTO
     */
    private PostDto convertToPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getAuthor().getId(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    /**
     * 将Comment实体转换为CommentDto
     * @param comment 评论实体
     * @return 评论DTO
     */
    private CommentDto convertToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getAuthor().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
