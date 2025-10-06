package org.bigseven.util;

import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.entity.Feedback;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 反馈响应构建器
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Component
public class FeedbackResponseBuilder {

    private final UserAuthenticationQueryUtils userAuthenticationQueryUtils;
    private final FeedbackImageUtils feedbackImageUtils;

    /**
     * 内容标记常量
     */
    public static final String NULL_CONTENT_MARKER = "nulla";

    public FeedbackResponseBuilder(UserAuthenticationQueryUtils userAuthenticationQueryUtils, FeedbackImageUtils feedbackImageUtils) {
        this.userAuthenticationQueryUtils = userAuthenticationQueryUtils;
        this.feedbackImageUtils = feedbackImageUtils;
    }

    /**
     * 构建基础反馈响应
     */
    public void buildBaseFeedbackResponse(Object response, Feedback feedback) {
        if (feedback == null) {
            return;
        }

        UserAuthenticationQueryUtils.UserPermissionInfo permissionInfo = userAuthenticationQueryUtils.getCurrentUserPermissionInfo();
        Integer displayUserId = userAuthenticationQueryUtils.processUserIdDisplay(feedback, permissionInfo);

        // 设置响应对象的userId（必须设置，即使为null）
        setUserIdToResponse(response, displayUserId);

        // 设置用户信息
        userAuthenticationQueryUtils.setUserInfoToResponse(response, feedback, displayUserId);

        // 设置图片URL
        List<String> imageUrls = feedbackImageUtils.getFeedbackImageUrls(feedback.getFeedbackId());
        setImageUrlsToResponse(response, imageUrls);
    }

    /**
     * 转换到GetAllFeedbackResponse
     */
    public GetAllFeedbackResponse convertToGetAllResponse(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        GetAllFeedbackResponse response = new GetAllFeedbackResponse();
        BeanUtils.copyProperties(feedback, response);

        // 处理内容显示
        if (NULL_CONTENT_MARKER.equals(feedback.getContent())) {
            response.setContent(null);
        }

        buildBaseFeedbackResponse(response, feedback);
        return response;
    }

    /**
     * 构建详情响应
     */
    public GetFeedbackDetailResponse buildDetailResponse(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        GetFeedbackDetailResponse response = GetFeedbackDetailResponse.builder()
                .feedbackId(feedback.getFeedbackId())
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .isUrgent(feedback.getIsUrgent())
                .isNicked(feedback.getIsNicked())
                .isConfirmed(feedback.getIsConfirmed())
                .viewCount(feedback.getViewCount())
                .userId(feedback.getUserId())
                .acceptedByUserId(feedback.getAcceptedByUserId())
                .feedbackType(feedback.getFeedbackType())
                .feedbackStatus(feedback.getFeedbackStatus())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .processedAt(feedback.getProcessedAt())
                .build();

        // 处理内容显示
        if (NULL_CONTENT_MARKER.equals(feedback.getContent())) {
            response.setContent(null);
        }

        buildBaseFeedbackResponse(response, feedback);
        return response;
    }

    /**
     * 设置用户ID到响应对象
     */
    private void setUserIdToResponse(Object response, Integer userId) {
        if (response instanceof GetAllFeedbackResponse) {
            ((GetAllFeedbackResponse) response).setUserId(userId);
        } else if (response instanceof GetFeedbackDetailResponse) {
            ((GetFeedbackDetailResponse) response).setUserId(userId);
        }
    }

    private void setImageUrlsToResponse(Object response, List<String> imageUrls) {
        if (response instanceof GetAllFeedbackResponse) {
            ((GetAllFeedbackResponse) response).setImageUrls(
                    imageUrls != null ? imageUrls : new ArrayList<>()
            );
        } else if (response instanceof GetFeedbackDetailResponse) {
            ((GetFeedbackDetailResponse) response).setImageUrls(
                    imageUrls != null ? imageUrls : new ArrayList<>()
            );
        }
    }
}