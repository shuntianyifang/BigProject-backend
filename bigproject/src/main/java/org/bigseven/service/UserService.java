package org.bigseven.service;

import org.bigseven.constant.UserTypeEnum;

/**
 * @author v185v
 * &#064;date  2025/9/16
 */
public interface UserService {

    /**
     * 用户登录验证
     * 根据用户名/邮箱和密码验证用户身份，验证成功后返回用户ID
     *
     * @param username 用户名（可选，与邮箱二选一）
     * @param password 用户密码
     * @param email 邮箱地址（可选，与用户名二选一）
     * @return 登录成功返回用户ID，失败返回null或错误码
     */
    Integer login(String username, String password, String email);

    /**
     * 用户注册
     * 创建新的用户账号，设置用户名、密码、邮箱和用户类型
     *
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱地址
     * @param userType 用户类型枚举
     * @return 注册成功返回用户ID，失败返回null或错误码
     */
    Integer register(String username, String password, String email, UserTypeEnum userType);
}
