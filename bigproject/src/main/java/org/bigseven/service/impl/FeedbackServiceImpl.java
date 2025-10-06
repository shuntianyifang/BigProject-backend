package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.config.FeedbackConfig;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.adminreply.AdminReplyVO;
import org.bigseven.dto.feedback.GetAllFeedbackRequest;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.entity.Feedback;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.FeedbackMapper;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackService;
import org.bigseven.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author v185v
 * &#064;date  2025/9/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final FeedbackConfig feedbackConfig;
    private final FeedbackResponseBuilder feedbackResponseBuilder;
    private final FeedbackImageUtils feedbackImageUtils;
    private final UserAuthenticationQueryUtils userAuthenticationQueryUtils;
    private final AdminReplyUtils adminReplyUtils;

    private static final String ASC_ORDER = "asc";

    /**
     * 将Feedback对象转换为GetAllFeedbackResponse对象
     */
    private GetAllFeedbackResponse convertToResponse(Feedback feedback) {
        // 转换基础信息
        GetAllFeedbackResponse response = feedbackResponseBuilder.convertToGetAllResponse(feedback);

        // 查询并设置管理员回复
        List<AdminReplyVO> adminReplies = adminReplyUtils.getRepliesByFeedbackId(feedback.getFeedbackId());
        response.setAdminReply(adminReplies);

        return response;
    }

    /**
     * 发布用户反馈信息
     *
     * @param userId 用户ID
     * @param isNicked 是否匿名发布
     * @param isUrgent 是否紧急反馈
     * @param feedbackType 反馈类型枚举
     * @param title 反馈标题
     * @param content 反馈内容
     * @param imageUrls 反馈图片URL列表，最多保存前9张图片
     */
    @Override
    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isUrgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls) {
        Feedback feedback = Feedback.builder()
                .userId(userId)
                .isNicked(isNicked)
                .isUrgent(isUrgent)
                .isConfirmed(false)
                .viewCount(0)
                .feedbackType(feedbackType)
                .feedbackStatus(FeedbackStatusEnum.PENDING)
                .title(title)
                .content(content)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imageUrls(imageUrls)
                .build();
        feedbackMapper.insert(feedback);

        /// 保存图片信息
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < Math.min(imageUrls.size(), feedbackConfig.getMaxImages()); i++) {
                feedbackImageUtils.saveFeedbackImages(feedback.getFeedbackId(), imageUrls, feedbackConfig.getMaxImages());
            }
        }
    }

    /**
     * 删除反馈信息
     * @param id 反馈ID
     * @param userDetails 用户信息
     */
    @Override
    public void deleteFeedback(Integer id, CustomUserDetails userDetails) {
        Feedback feedback = feedbackMapper.selectById(id);
        Integer userId = userDetails.getUserId();
        if (feedback != null) {
            if (feedback.getUserId().equals(userId) || userDetails.getAuthorities().stream()
                    .anyMatch(auth -> "ROLE_SUPER_ADMIN".equals(auth.getAuthority()))) {
                feedbackMapper.deleteById(id);
            } else {
                throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
            }
        } else {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }
    }

    /**
     * 普通User更新反馈信息
     * @param id 反馈ID
     * @param feedbackType 反馈类型
     * @param title 反馈标题
     * @param content 反馈内容
     */
    @Override
    public void updateFeedback(Integer id, CustomUserDetails userDetails, Boolean isNicked, Boolean isUrgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls) {

        Feedback feedback = feedbackMapper.selectById(id);
        Integer userId = userDetails.getUserId();

        if (feedback == null) {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }
        if (!feedback.getUserId().equals(userId)) {
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        if (feedback.getFeedbackStatus() != FeedbackStatusEnum.PENDING) {
            throw new ApiException(ExceptionEnum.FEEDBACK_CANNOT_UPDATE);
        }

        feedback.setIsNicked(isNicked);
        feedback.setIsUrgent(isUrgent);
        feedback.setFeedbackType(feedbackType);
        feedback.setTitle(title);
        feedback.setContent(content);
        feedback.setUpdatedAt(LocalDateTime.now());
        feedbackMapper.updateById(feedback);

        // 更新图片信息
        if (imageUrls != null) {
            // 先删除旧图片
            feedbackImageUtils.deleteImagesByFeedbackId(id);
            // 再保存新图片
            for (int i = 0; i < Math.min(imageUrls.size(), feedbackConfig.getMaxImages()); i++) {
                feedbackImageUtils.saveFeedbackImages(id, imageUrls, feedbackConfig.getMaxImages());
            }
        }
    }

    /**
     * 根据条件查询所有反馈信息并分页返回
     * @param request 包含查询条件和分页参数的请求对象
     * @return 分页的反馈响应对象列表
     */
    @Override
    public Page<GetAllFeedbackResponse> getAllFeedbacks(GetAllFeedbackRequest request) {
        // 创建分页对象
        Page<Feedback> page = new Page<>(
                request.getPage() != null ? request.getPage() : 1,
                request.getSize() != null ? request.getSize() : 10
        );

        // 构建查询条件
        QueryWrapper<Feedback> queryWrapper = buildQueryWrapper(request);

        // 执行查询
        IPage<Feedback> feedbackPage = feedbackMapper.selectPage(page, queryWrapper);

        // 转换为响应对象
        return convertToResponsePage(feedbackPage);
    }

    /**
     * 根据ID获取反馈详情
     * @param id 反馈ID
     * @return 反馈详情响应对象
     * @throws ApiException 当反馈不存在时抛出异常
     */
    @Override
    public GetFeedbackDetailResponse getFeedbackDetail(Integer id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }

        // 增加浏览次数
        feedbackMapper.incrementViewCount(id);

        GetFeedbackDetailResponse response = feedbackResponseBuilder.buildDetailResponse(feedback);

        // 设置管理员回复信息
        List<AdminReplyVO> adminReplies = adminReplyUtils.getRepliesByFeedbackId(id);
        response.setAdminReply(adminReplies);

        return response;
    }

    @Override
    public void confirmFeedback(Integer id, CustomUserDetails userDetails) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }
        if (feedback.getIsConfirmed()) {
            throw new ApiException(ExceptionEnum.FEEDBACK_ALREADY_CONFIRMED);
        }
        if (!userDetails.getUserId().equals(feedback.getUserId())) {
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        feedback.setIsConfirmed(true);
        feedback.setUpdatedAt(LocalDateTime.now());
        feedbackMapper.updateById(feedback);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<Feedback> buildQueryWrapper(GetAllFeedbackRequest request) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();

        // 条件筛选
        applyLikeCondition(queryWrapper, "title", request.getTitleKeyword());
        applyLikeCondition(queryWrapper, "content", request.getContentKeyword());
        applyEqualCondition(queryWrapper, "is_urgent", request.getIsUrgent());
        applyEqualCondition(queryWrapper, "is_nicked", request.getIsNicked());
        applyEqualCondition(queryWrapper, "accepted_by_user_id", request.getAdminId());
        applyDateCondition(queryWrapper, "created_at", request.getFromTime(), request.getToTime());

        // 处理状态标签（整数列表，直接用in条件）
        if (request.getStatusTags() != null && !request.getStatusTags().isEmpty()) {
            queryWrapper.in("feedback_status", request.getStatusTags());
        }

        // 处理类型标签（整数列表，直接用in条件）
        if (request.getTypeTags() != null && !request.getTypeTags().isEmpty()) {
            queryWrapper.in("feedback_type", request.getTypeTags());
        }

        // 处理学生ID查询时的匿名权限
        if (request.getStudentId() != null) {
            UserAuthenticationQueryUtils.UserPermissionInfo permissionInfo =
                    userAuthenticationQueryUtils.getCurrentUserPermissionInfo();

            // 如果是管理员，可以直接查看所有反馈
            if (permissionInfo.isAdmin()) {
                queryWrapper.eq("user_id", request.getStudentId());
            }
            // 如果是普通用户查询自己的反馈
            else if (permissionInfo.isAuthenticated() &&
                    permissionInfo.getCurrentUserId() != null &&
                    permissionInfo.getCurrentUserId().equals(request.getStudentId())) {
                queryWrapper.eq("user_id", request.getStudentId());
            }
            // 如果是普通用户查询他人的反馈，需要过滤匿名反馈
            else {
                queryWrapper.and(wrapper -> wrapper
                        .eq("user_id", request.getStudentId())
                        .eq("is_nicked", false)
                        // 只能看到非匿名的反馈
                );
            }
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
    private Page<GetAllFeedbackResponse> convertToResponsePage(IPage<Feedback> feedbackPage) {
        Page<GetAllFeedbackResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(feedbackPage, responsePage);

        List<GetAllFeedbackResponse> feedbackResponses = feedbackPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        responsePage.setRecords(feedbackResponses);
        return responsePage;
    }

    private void applyLikeCondition(QueryWrapper<Feedback> wrapper, String field, String value) {
        if (StringUtils.hasText(value)) {
            wrapper.like(field, value);
        }
    }

    private void applyEqualCondition(QueryWrapper<Feedback> wrapper, String field, Object value) {
        if (value != null) {
            wrapper.eq(field, value);
        }
    }

    private void applyDateCondition(QueryWrapper<Feedback> wrapper, String field, String start, String end) {
        if (StringUtils.hasText(start)) {
            wrapper.ge(field, start);
        }
        if (StringUtils.hasText(end)) {
            wrapper.le(field, end);
        }
    }

}
