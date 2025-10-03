package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.UserSimpleVO;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.UserMapper;
import org.bigseven.security.JwtTokenUtil;
import org.bigseven.security.JwtUserDetailsServiceImpl;
import org.bigseven.service.UserService;
import org.bigseven.util.UserConverterUtils;
import org.bigseven.util.XssProtectionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v185v
 * &#064;date   2025/9/16
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsServiceImpl userDetailsService;
    private final XssProtectionUtils xssProtectionUtils;
    private final UserConverterUtils userConverterUtils;

    /**
     * 用户登录（JWT认证）
     * @param username 用户名
     * @param password 密码
     * @return 包含token和用户信息的Map
     */
    @Override
    public Map<String, Object> login(String username, String password) {

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }

        // 检查用户是否被删除
        if (Boolean.TRUE.equals(user.getDeleted())) {
            throw new ApiException(ExceptionEnum.USER_DISABLED);
        }

        try {

            // 使用Spring Security进行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 生成JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            // 更新最后登录时间
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);

            UserSimpleVO userVO = userConverterUtils.toUserSimpleVO(user);

            // 返回结果
            Map<String, Object> result = new HashMap<>(8);
            result.put("token", token);
            result.put("user", userVO);
            result.put("expiration", jwtTokenUtil.getExpiration());

            return result;

        } catch (BadCredentialsException e) {
            throw new ApiException(ExceptionEnum.USERNAME_OR_PASSWORD_WRONG);
        }
    }

    private String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // 只允许字母、数字、下划线、中文，其他字符去除
        return input.replaceAll("[<>\"'%;()&+]", "");
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(input);
    }

    /**
     * 用户注册功能
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param userType 用户类型
     * @return 注册成功返回用户信息，用户名已存在返回null
     */
    @Override
    public Map<String, Object> register(String username, String password, String email, UserTypeEnum userType) {
        // 对输入做过滤
        username = xssProtectionUtils.sanitize(username);
        email = xssProtectionUtils.sanitize(email);
        // 再做HTML转义
        username = xssProtectionUtils.escapeHtml(username);
        email = xssProtectionUtils.escapeHtml(email);

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, username);
        User existingUser = userMapper.selectOne(userQueryWrapper);

        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (email != null && !email.isEmpty()) {
            LambdaQueryWrapper<User> emailQueryWrapper = new LambdaQueryWrapper<>();
            emailQueryWrapper.eq(User::getEmail, email);
            User emailUser = userMapper.selectOne(emailQueryWrapper);
            if (emailUser != null) {
                throw new RuntimeException("邮箱已被注册");
            }
        }

        // 创建新用户
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .userType(userType != null ? userType : UserTypeEnum.STUDENT)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        userMapper.insert(user);

        // 生成JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);

        // 返回结果
        Map<String, Object> result = new HashMap<>(8);
        result.put("token", token);
        result.put("user", user);
        result.put("message", "注册成功");

        return result;
    }

    /**
     * 刷新token
     * @param oldToken 旧token
     * @return 新token
     */
    @Override
    public Map<String, Object> refreshToken(String oldToken) {
        String username = jwtTokenUtil.getUsernameFromToken(oldToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.validateToken(oldToken, userDetails)) {
            String newToken = jwtTokenUtil.refreshToken(oldToken);

            Map<String, Object> result = new HashMap<>(8);
            result.put("token", newToken);
            result.put("username", username);
            return result;
        }

        throw new RuntimeException("Token刷新失败");
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userMapper.selectByUsername(username);
        }
        return null;
    }
}