package com.example.myapp_hou.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data       //添加 getter 和 setter 方法

/**
 * 用户实体类，对应数据库表 users
 */
@Entity
@Table(name = "users")//将一个 Java 类映射到数据库中的某张表
public class User implements UserDetails {
    @Id//表示这个字段是数据库表的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//表示主键值由数据库自动生成
    private Long id;

    @Column(unique = true, nullable = false)//字段名唯一，不能为空
    private String username;

    @Column(nullable = false)//不能为空
    private String password;

    private String avatar;      //头像存放的路径

    @Column(name = "created_at")
    private LocalDateTime createdAt;        //数据库中创建时间字段

    // 一对多关系：一个用户有多个视频
    /**
     * mappedBy: 这个关系已经在 Video 类的 user 字段中定义了，我这边只是引用它。
     * cascade: 表示当你对"用户"做某些操作时，自动对他的视频也执行相同操作。
     * fetch.lazy: 表示：查用户时，不立刻查他的所有视频,只有当你调用 user.getVideos() 时，才去数据库查视频列表
     */
   // @JsonIgnore
   //移除懒加载
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Video> videos = new ArrayList<>();

    // 论坛相关关系
    // 一对多关系：一个用户可以发表多个帖子
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // 避免序列化时出现循环引用
    private List<Post> posts = new ArrayList<>();

    // 一对多关系：一个用户可以发表多个评论
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // 避免序列化时出现循环引用
    private List<Comment> comments = new ArrayList<>();

    //构造函数
    public  User(){}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //重写
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
