package com.example.myapp_hou.repository;

import com.example.myapp_hou.entity.Post;
import com.example.myapp_hou.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 帖子数据访问接口
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 按创建时间倒序查询所有帖子
     * @return 帖子列表
     */
    List<Post> findByOrderByCreatedAtDesc();
    
    /**
     * 根据作者查询帖子并按创建时间倒序排列
     * @param author 帖子作者
     * @return 帖子列表
     */
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);
}
