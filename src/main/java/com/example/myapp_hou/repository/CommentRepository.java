package com.example.myapp_hou.repository;

import com.example.myapp_hou.entity.Comment;
import com.example.myapp_hou.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论数据访问接口
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 根据帖子查询评论并按创建时间正序排列
     * @param post 帖子
     * @return 评论列表
     */
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
