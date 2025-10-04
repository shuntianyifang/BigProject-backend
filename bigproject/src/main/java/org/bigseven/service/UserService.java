package org.bigseven.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.GetUserDetailResponse;
import org.bigseven.entity.User;
import org.bigseven.security.CustomUserDetails;

import java.util.Map;

/**
 * @author v185v
 * &#064;date  2025/9/16
 */
public interface UserService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> login(String username, String password);

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param userType 用户类型
     * @return 注册结果
     */
    Map<String, Object> register(String username, String nickname, String password, String email, UserTypeEnum userType);

    Map<String, Object> resetPassword(Integer userId, String password, String newPassword);

    /**
     * 刷新token
     * @param oldToken 旧Token
     * @return 刷新后的Token
     */
    Map<String, Object> refreshToken(String oldToken);

    /**
     * 获取当前登录用户
     * @return 用户信息
     */
    User getCurrentUser();

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户的唯一标识ID
     * @return 用户的详细信息响应对象
     */
    GetUserDetailResponse getUserDetail(Integer id);

    void updateUserDetail(Integer id, CustomUserDetails userDetails, String email, String userPhone, String nickname, String realName);
}