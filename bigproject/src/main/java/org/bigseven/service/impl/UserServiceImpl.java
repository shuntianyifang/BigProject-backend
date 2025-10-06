package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.adminreply.AdminReplyVO;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.user.GetAllUserRequest;
import org.bigseven.dto.user.GetAllUserResponse;
import org.bigseven.dto.user.GetUserDetailResponse;
import org.bigseven.dto.user.UserSimpleVO;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.UserMapper;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.security.JwtTokenUtil;
import org.bigseven.security.JwtUserDetailsServiceImpl;
import org.bigseven.service.UserService;
import org.bigseven.util.UserAuthenticationQueryUtils;
import org.bigseven.util.UserConverterUtils;
import org.bigseven.util.UserResponseBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author v185v
 * &#064;date   2025/9/16
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsServiceImpl userDetailsService;
    private final UserConverterUtils userConverterUtils;
    private final UserAuthenticationQueryUtils userAuthenticationQueryUtils;
    private final UserResponseBuilder userResponseBuilder;

    private static final String ASC_ORDER = "asc";

    /**
     * 将User对象转换为GetAllUserResponse对象
     */
    private GetAllUserResponse convertToResponse(User user) {
        // 转换基础信息
        GetAllUserResponse response = userResponseBuilder.convertToGetAllResponse(user);

        return response;
    }

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
            user.setLastLoginAt(LocalDateTime.now());
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

    /**
     * 用户注册功能
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @param userType 用户类型
     * @return 注册成功返回用户信息，用户名已存在返回null
     */
    @Override
    public Map<String, Object> register(String username, String nickname, String password, String email, UserTypeEnum userType) {

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, username);
        User existingUser = userMapper.selectOne(userQueryWrapper);

        if (existingUser != null) {
            throw new ApiException(ExceptionEnum.USER_EXIST);
        }

        // 检查邮箱是否已存在
        if (email != null && !email.isEmpty()) {
            LambdaQueryWrapper<User> emailQueryWrapper = new LambdaQueryWrapper<>();
            emailQueryWrapper.eq(User::getEmail, email);
            User emailUser = userMapper.selectOne(emailQueryWrapper);
            if (emailUser != null) {
                throw new ApiException(ExceptionEnum.EMAIL_EXIST);
            }
        }

        // 创建新用户
        User user = User.builder()
                .username(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .userType(userType != null ? userType : UserTypeEnum.STUDENT)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        userMapper.insert(user);

        UserSimpleVO userVO = userConverterUtils.toUserSimpleVO(user);

        // 生成JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);

        // 返回结果
        Map<String, Object> result = new HashMap<>(8);
        result.put("token", token);
        result.put("user", userVO);
        result.put("message", "注册成功");

        return result;
    }

    /**
     * 用户重置密码
     * @param userId 用户名
     * @param password 密码
     * @param newPassword 新密码
     * @return 成功提示
     */
    @Override
    public Map<String, Object> resetPassword(Integer userId, String password, String newPassword) {
        User user = userMapper.selectById(userId);
        // 检查用户是否存在
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }

        // 检查用户是否被删除
        if (Boolean.TRUE.equals(user.getDeleted())) {
            throw new ApiException(ExceptionEnum.USER_DISABLED);
        }

        // 验证旧密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiException(ExceptionEnum.USERNAME_OR_PASSWORD_WRONG);
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        Map<String, Object> result = new HashMap<>(4);
        result.put("message", "密码重置成功");
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

    /**
     * 根据ID获取反馈详情
     * @param id 反馈ID
     * @return 反馈详情响应对象
     * @throws ApiException 当反馈不存在时抛出异常
     */
    @Override
    public GetUserDetailResponse getUserDetail(Integer id) {
        User user = userMapper.selectById(id);
        UserAuthenticationQueryUtils.UserPermissionInfo permissionInfo = userAuthenticationQueryUtils.getCurrentUserPermissionInfo();
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }
        GetUserDetailResponse response = GetUserDetailResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userPhone(user.getUserPhone())
                .nickname(user.getNickname())
                .realName(user.getRealName())
                .userType(user.getUserType())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
        if (!permissionInfo.isAdmin() && !permissionInfo.getCurrentUserId().equals(id)) {
            // 非管理员且非用户本人，隐藏敏感信息
            response.setUsername(null);
            response.setRealName(null);
        }
        return response;
    }

    /**
     * 根据条件查询所有反馈信息并分页返回
     * @param request 包含查询条件和分页参数的请求对象
     * @return 分页的反馈响应对象列表
     */
    @Override
    public Page<GetAllUserResponse> getAllUsers(GetAllUserRequest request) {
        // 创建分页对象
        Page<User> page = new Page<>(
                request.getPage() != null ? request.getPage() : 1,
                request.getSize() != null ? request.getSize() : 10
        );

        // 构建查询条件
        QueryWrapper<User> queryWrapper = buildQueryWrapper(request);

        // 执行查询
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);

        // 转换为响应对象
        return convertToResponsePage(userPage);
    }

    @Override
    public void updateUserDetail(Integer id, CustomUserDetails userDetails, String email, String userPhone, String nickname, String realName) {
        Integer userId = userDetails.getUserId();
        if (!Objects.equals(userId, id)){
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }
        user.setEmail(email);
        user.setUserPhone(userPhone);
        user.setNickname(nickname);
        user.setRealName(realName);
        userMapper.updateById(user);
    }

    @Override
    public void changeUserType(Integer id, UserTypeEnum userType) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }
        if (!userType.equals(UserTypeEnum.ADMIN) && !userType.equals(UserTypeEnum.STUDENT)) {
            throw new ApiException(ExceptionEnum.ILLEGAL_USER_TYPE);
        }
        if (user.getUserType() == UserTypeEnum.SUPER_ADMIN) {
            throw new ApiException(ExceptionEnum.OPERATION_FAILED);
        }
        user.setUserType(userType);
        userMapper.updateById(user);
    }

    @Override
    public  void deleteUser(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }
        if (user.getUserType() == UserTypeEnum.SUPER_ADMIN) {
            throw new ApiException(ExceptionEnum.OPERATION_FAILED);
        }
        user.setDeleted(true);
        userMapper.updateById(user);
    }

    @Override
    public void unregisterUser(Integer id, CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        if (!Objects.equals(userId, id)){
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        User user = userMapper.selectById(id);
        user.setDeleted(true);
        userMapper.updateById(user);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<User> buildQueryWrapper(GetAllUserRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 条件筛选
        applyLikeCondition(queryWrapper, "username", request.getUsernameKeyword());
        applyDateCondition(queryWrapper, "created_at", request.getFromTime(), request.getToTime());
        if (request.getTypeTags() != null && !request.getTypeTags().isEmpty()) {
            queryWrapper.in("user_type", request.getTypeTags());
        }

        // 设置排序
        String sortField = request.getSortField() != null ? request.getSortField() : "created_at";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder() : "desc";

        if (ASC_ORDER.equalsIgnoreCase(sortOrder)) {
            queryWrapper.orderByAsc(sortField);
        } else {
            queryWrapper.orderByDesc(sortField);
        }

        return queryWrapper;
    }

    /**
     * 转换为响应分页
     */
    private Page<GetAllUserResponse> convertToResponsePage(IPage<User> userPage) {
        Page<GetAllUserResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(userPage, responsePage);

        List<GetAllUserResponse> userResponses = userPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        responsePage.setRecords(userResponses);
        return responsePage;
    }

    private void applyLikeCondition(QueryWrapper<User> wrapper, String field, String value) {
        if (StringUtils.hasText(value)) {
            wrapper.like(field, value);
        }
    }

    private void applyEqualCondition(QueryWrapper<User> wrapper, String field, Object value) {
        if (value != null) {
            wrapper.eq(field, value);
        }
    }

    private void applyDateCondition(QueryWrapper<User> wrapper, String field, String start, String end) {
        if (StringUtils.hasText(start)) {
            wrapper.ge(field, start);
        }
        if (StringUtils.hasText(end)) {
            wrapper.le(field, end);
        }
    }
}