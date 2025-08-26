package com.example.myapp_hou.dto;

import com.example.myapp_hou.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 响应 DTO（返回给前端的用户信息）
 * 目的：只把前端需要的信息返回，隐藏敏感字段（如密码）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;        // id
    private String username;        //用户名
    private String avatar;      //头像

    private String createdAt;       // 时间
    private List<VideoDto> videos;
    private int videoCount;
    // 添加获取完整URL的方法
    //看前端需不需要
    public String getAvatarUrl() {
        return avatar != null ? "/api/avatar/" + avatar : null;
    }

    public static  UserResponse from(User user) //用于把 User 实体转换成 UserResponse DTO
    {
        UserResponse res=new UserResponse();//创建一个 UserResponse 对象
        //这个作用是：把 User 的 id、username、avatar、createdAt 属性赋给 UserResponse 对象
        //不包含密码
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setAvatar(user.getAvatar());
        res.setCreatedAt(user.getCreatedAt().toString());//LocalDateTime 转换成 String

        if (user.getVideos() != null) {
            res.setVideos(user.getVideos().stream()
                    .map(VideoDto::from)
                    .collect(Collectors.toList()));
            res.setVideoCount(user.getVideos().size());
        }
        return res;
    }
}
