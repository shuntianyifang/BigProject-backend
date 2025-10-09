package org.bigseven.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.GetAllUserRequest;
import org.bigseven.dto.user.GetAllUserResponse;
import org.bigseven.dto.user.GetUserDetailResponse;
import org.bigseven.security.CustomUserDetails;

import java.util.Map;

/**
 * 用户服务接口
 *
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
     * @param nickname 昵称
     * @param password 密码
     * @param email 邮箱
     * @param userType 用户类型
     * @return 注册结果
     */
    Map<String, Object> register(String username, String nickname, String password, String email, UserTypeEnum userType);

    /**
     * 重置密码
     * @param userId 用户ID
     * @param password 旧密码
     * @param newPassword 新密码
     * @return 重置密码结果
     */
    Map<String, Object> resetPassword(Integer userId, String password, String newPassword);

    /**
     * 刷新token
     * @param oldToken 旧Token
     * @return 刷新后的Token
     */
    Map<String, Object> refreshToken(String oldToken);

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户的唯一标识ID
     * @return 用户的详细信息响应对象
     */
    GetUserDetailResponse getUserDetail(Integer id);

    /**
     * 更新用户详情
     *
     * @param id 用户的唯一标识ID
     * @param userDetails 执行更新操作的用户
     * @param email 用户的邮箱地址
     * @param userPhone 用户的手机号码
     * @param nickname 用户的昵称
     * @param realName 用户的姓名
     */
    void updateUserDetail(Integer id, CustomUserDetails userDetails, String email, String userPhone, String nickname, String realName);

    /**
     * 查看所有用户（分页+条件查询）
     *
     * @param request 用户查询请求，包含分页参数和筛选条件
     * @return 分页的用户响应列表，包含用户详情和分页信息
     */
    Page<GetAllUserResponse> getAllUsers(GetAllUserRequest request);

    /**
     * 修改用户类型
     *
     * @param id 用户的ID
     * @param userType 新的用户类型
     */
    void changeUserType(Integer id, UserTypeEnum userType);

    /**
     * 删除用户
     *
     * @param id 用户的ID
     */
    void deleteUser(Integer id);

    /**
     * 注销用户
     *
     * @param id 用户的ID
     * @param userDetails 执行注销操作的用户
     */
    void unregisterUser(Integer id, CustomUserDetails userDetails);
}