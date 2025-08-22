package com.example.myapp_hou.service.impl;

import com.example.myapp_hou.dto.RegisterRequest;//请求
import com.example.myapp_hou.dto.UpdateUserRequest;
import com.example.myapp_hou.dto.UserResponse;//响应
import com.example.myapp_hou.entity.User;
import com.example.myapp_hou.repository.UserRepository;

import com.example.myapp_hou.service.MediaService;
import com.example.myapp_hou.service.UserService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 * 业务逻辑层（处理注册、登录、验证等）
 */

@Service//标识为服务类
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;//用来操作数据库（保存用户、查询用户名是否存在等
    @Autowired
    private PasswordEncoder passwordEncoder;//用来对密码进行加密
    @Autowired

    private MediaService mediaService;
    /**
     *注册用户
     */
    @Override
    //参数是 RegisterRequest（前端传来的注册数据）。
    //返回 UserResponse（返回给前端的用户信息，不含密码）
    //假如前端传来的数据是：
    //request.setUsername("tom");        // Spring 自动调用
    //request.setPassword("123456");
    public UserResponse register(RegisterRequest  request)  //注册用户
    {
       if(existsByUsername(request.getUsername()))//调用自己封装的方法 existsByUsername
        {
            throw new RuntimeException("用户名已存在");
        }
       //加密 密码
        String encodedPassword = passwordEncoder.encode(request.getPassword());//对密码进行编码
      // 创建用户
        User user = new User(request.getUsername(), encodedPassword);//创建用户对象
      //用户创建时间
      user.setCreatedAt(LocalDateTime.now());

        //调用 UserRepository 的 save 方法，把用户数据写入数据库
        User savedUser = userRepository.save(user);
       //返回响应 DTO
        return UserResponse.from(savedUser);//把 User 实体转换成 UserResponse DTO
    }
    @Override
    public  boolean existsByUsername(String username){
        //判断用户名是否存在,返回的是 Optional<User>
        return userRepository.findByUsername( username).isPresent();
    }

    /**
     * 更新密码，需要用到密码验证，所以放在用户服务类中
     */
    @Override
    public UserResponse updateUser(String currentUsername, UpdateUserRequest request){
        // 1. 获取当前用户
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 验证当前密码（判断前端发送过来的密码和从数据库获取到密码）
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("当前密码不正确");
        }

        // 3. 更新用户名（如果提供了新用户名）不为空且不为null
        if (request.getNewUsername() != null && !request.getNewUsername().isEmpty()) {
           //调用 existsByUsername 方法判断是否存在同名用户
            if (existsByUsername(request.getNewUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            //更新用户名
            user.setUsername(request.getNewUsername());
        }

        // 4. 更新密码（如果提供了新密码）
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        // 5. 保存更新
        User updatedUser = userRepository.save(user);
        return UserResponse.from(updatedUser);
    }

    /**
     * 更新（上传）头像
     */
    @Override
    public UserResponse updateAvatar(String currentUsername, MultipartFile avatar) throws IOException {
       //获取当前用户
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        //删除旧头像
        if (user.getAvatar() != null) {
            mediaService.deleteAvatar(user.getAvatar());
        }
        //保存新头像
        String avatarPath = mediaService.storeAvatar(avatar, user.getUsername());
        user.setAvatar(avatarPath);

        //保存更新
        User updatedUser = userRepository.save(user);
        return UserResponse.from(updatedUser);
    }

    /**
     * 获取头像文件名
     */
    @Override
    public String getAvatarUrl(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            return null; // 或者返回默认头像URL
        }

        // 返回完整的头像访问URL
        return user.getAvatar(); // 只返回文件名，不是完整URL
    }

    // 在 UserServiceImpl.java 中添加事务支持
    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserResponse(User user) {
        // 强制初始化懒加载的videos集合
        if (user.getVideos() != null) {
            user.getVideos().size(); // 这会触发懒加载
        }
        return UserResponse.from(user); // ✅ 在事务内，可以安全调用 getVideos()
    }
}
