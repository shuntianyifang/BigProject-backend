package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.entity.AdminReply;
import org.bigseven.entity.Feedback;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.AdminReplyMapper;
import org.bigseven.mapper.FeedbackMapper;
import org.bigseven.service.FeedbackAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackAdminServiceImpl implements FeedbackAdminService {

    private final FeedbackMapper feedbackMapper;
    private final AdminReplyMapper adminReplyMapper;

    /**
     * 处理反馈状态和管理员回复
     *
     * @param feedbackId 反馈ID
     * @param feedbackStatus 反馈状态
     * @param adminReply 管理员回复信息
     * @param acceptedByUserId 处理人ID
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public void processFeedback(Integer feedbackId, FeedbackStatusEnum feedbackStatus,
                                AdminReply adminReply, Integer acceptedByUserId) {

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSuperAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> "ROLE_SUPER_ADMIN".equals(grantedAuthority.getAuthority()));

        // 查找反馈
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }

        if (feedbackStatus == FeedbackStatusEnum.SPAM_APPROVED && !isSuperAdmin) {
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        // 使用UpdateWrapper来使null能正确覆盖原先数据
        UpdateWrapper<Feedback> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("feedback_id", feedbackId);

        if (feedbackStatus != FeedbackStatusEnum.PENDING) {

            // 更新反馈状态和处理人
            updateWrapper.set("feedback_status", feedbackStatus)
                    .set("accepted_by_user_id", acceptedByUserId)
                    .set("processed_at", LocalDateTime.now());

        }
        else {

            // 清除反馈状态和处理人
            updateWrapper.set("feedback_status", feedbackStatus)
                    .set("accepted_by_user_id", null)
                    .set("processed_at", null);

        }

        // 如果有管理员回复，保存回复信息
        if (adminReply != null) {
            saveAdminReply(feedbackId, adminReply, acceptedByUserId);
        }

        // 如果是超级管理员将反馈标记为垃圾信息且没有提供回复，则自动生成一条预设回复
        else if (feedbackStatus == FeedbackStatusEnum.SPAM_APPROVED) {
            AdminReply autoReply = new AdminReply();
            autoReply.setTitle("反馈被标记为垃圾信息");
            autoReply.setContent("请您在提交反馈时确保内容的有效性和准确性，感谢您的理解和配合。如有异议，请重新反馈。");
            saveAdminReply(feedbackId, autoReply, acceptedByUserId);
        }

        // 更新反馈
        feedbackMapper.update(null, updateWrapper);
    }

    /**
     * 保存管理员回复到单独的表
     */
    private void saveAdminReply(Integer feedbackId, AdminReply adminReply, Integer acceptedByUserId) {
        AdminReply reply = new AdminReply();

        reply.setFeedbackId(feedbackId);
        reply.setTitle(adminReply.getTitle());
        reply.setContent(adminReply.getContent());
        reply.setUserId(acceptedByUserId);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());

        adminReplyMapper.insert(reply);
    }
}
