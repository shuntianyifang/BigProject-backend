package org.bigseven.util;

import lombok.Builder;
import lombok.Data;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.dto.user.UserSimpleVO;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.User;
import org.bigseven.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

/**
 * 反馈查询工具类
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Component
public class FeedbackQueryUtils {

    private final UserMapper userMapper;
    private final UserConverterUtils userConverterUtils;
    private final FeedbackImageUtils feedbackImageUtils;

    public FeedbackQueryUtils(UserMapper userMapper, UserConverterUtils userConverterUtils, FeedbackImageUtils feedbackImageUtils) {
        this.userMapper = userMapper;
        this.userConverterUtils = userConverterUtils;
        this.feedbackImageUtils = feedbackImageUtils;
    }

    /**
     * 获取当前用户权限信息
     */
    public UserPermissionInfo getCurrentUserPermissionInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = false;
        Integer currentUserId = null;

        if (authentication != null && authentication.isAuthenticated()) {
            // 只有认证用户才判断角色
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())
                            || "ROLE_SUPER_ADMIN".equals(auth.getAuthority()));

            currentUserId = getCurrentUserId(authentication);
        }

        return UserPermissionInfo.builder()
                .isAdmin(isAdmin)
                .currentUserId(currentUserId)
                .isAuthenticated(authentication != null && authentication.isAuthenticated())
                .build();
    }

    /**
     * 处理用户ID的显示逻辑
     */
    public Integer processUserIdDisplay(Feedback feedback, UserPermissionInfo permissionInfo) {
        Integer userId = feedback.getUserId();

        // 管理员始终可以看到userId
        if (permissionInfo.isAdmin()) {
            return userId;
        }

        // 非管理员情况下的处理
        boolean isPublisherAnonymous = feedback.getIsNicked() != null && feedback.getIsNicked();

        if (isPublisherAnonymous) {
            // 匿名发布的情况
            if (permissionInfo.isAuthenticated()) {
                // 已登录用户：只有发布者自己可以看到userId
                boolean isPublisher = permissionInfo.getCurrentUserId() != null &&
                        permissionInfo.getCurrentUserId().equals(feedback.getUserId());
                if (!isPublisher) {
                    userId = null;
                    // 非发布者看不到匿名发布的userId
                }
            } else {
                // 未登录用户：看不到匿名发布的userId
                userId = null;
            }
        }
        // 非匿名发布：所有人都可以看到userId

        return userId;
    }

    /**
     * 设置用户信息到响应对象
     */
    public void setUserInfoToResponse(Object response, Feedback feedback, Integer displayUserId) {
        // 设置学生信息
        if (displayUserId != null) {
            User studentUser = userMapper.selectById(feedback.getUserId());
            if (studentUser != null) {
                UserSimpleVO studentVO = userConverterUtils.toUserSimpleVO(studentUser);
                setStudentToResponse(response, studentVO);
            }
        } else {
            // 如果displayUserId为null，确保student也为null
            setStudentToResponse(response, null);
        }

        // 设置管理员信息（不受匿名影响）
        if (feedback.getAcceptedByUserId() != null) {
            User adminUser = userMapper.selectById(feedback.getAcceptedByUserId());
            if (adminUser != null) {
                UserSimpleVO adminVO = userConverterUtils.toUserSimpleVO(adminUser);
                setAdminToResponse(response, adminVO);
            }
        } else {
            setAdminToResponse(response, null);
        }
    }

    /**
     * 获取反馈图片URL
     */
    public List<String> getFeedbackImageUrls(Integer feedbackId) {
        return feedbackImageUtils.getFeedbackImageUrls(feedbackId);
    }

    private void setStudentToResponse(Object response, UserSimpleVO studentVO) {
        if (response instanceof GetAllFeedbackResponse) {
            ((GetAllFeedbackResponse) response).setStudent(studentVO);
        } else if (response instanceof GetFeedbackDetailResponse) {
            ((GetFeedbackDetailResponse) response).setStudent(studentVO);
        }
    }

    private void setAdminToResponse(Object response, UserSimpleVO adminVO) {
        if (response instanceof GetAllFeedbackResponse) {
            ((GetAllFeedbackResponse) response).setAdmin(adminVO);
        } else if (response instanceof GetFeedbackDetailResponse) {
            ((GetFeedbackDetailResponse) response).setAdmin(adminVO);
        }
    }

    /**
     * 获取当前用户ID
     */
    public Integer getCurrentUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            try {
                Method getUserIdMethod = principal.getClass().getMethod("getUserId");
                Object userId = getUserIdMethod.invoke(principal);
                return userId instanceof Integer ? (Integer) userId : null;
            } catch (Exception e) {
                log.warn("无法获取用户ID");
                return null;
            }
        }
        return null;
    }

    @Data
    @Builder
    public static class UserPermissionInfo {
        private boolean isAdmin;
        private Integer currentUserId;
        private boolean isAuthenticated;
    }
}
