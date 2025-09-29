package org.bigseven.controller;


import jakarta.validation.Valid;
import org.bigseven.dto.user.PublishFeedbackRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author v185v
 * &#064;date 2025/9/17
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 发布用户反馈信息
     *
     * @param request 包含反馈信息的请求对象，包含以下字段：
     *                - userId: 用户ID
     *                - isNicked: 是否匿名发布
     *                - isArgent: 是否紧急反馈
     *                - feedbackType: 反馈类型
     *                - title: 反馈标题
     *                - content: 反馈内容
     *                - imageUrls: 图片URL列表
     * @return AjaxResult<Void> 操作结果
     */
    @PostMapping("/publish")
    public AjaxResult<Void> publishFeedback(@Valid @RequestBody PublishFeedbackRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 从当前登录用户中获取user_id
        Integer userId = userDetails.getUserId();
        feedbackService.publishFeedback(userId,
                request.getIsNicked(),
                request.getIsUrgent(),
                request.getFeedbackType(),
                request.getTitle(),
                request.getContent(),
                request.getImageUrls()
        );
        return AjaxResult.success();
    }


    /**
     * 管理员标记帖子状态
     *
     * @param request 包含标记反馈所需信息的请求对象，包括反馈ID和反馈状态
     * @param userDetails 当前登录管理员的详细信息
     * @return AjaxResult<Void> 操作结果，成功时返回空数据的成功响应
     */
    @PostMapping("/mark")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public AjaxResult<Void> markFeedback(@Valid @RequestBody PublishFeedbackRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 从当前登录管理员中获取user_id，作为处理人ID
        Integer acceptedByUserId = userDetails.getUserId();
        feedbackService.markFeedback(request.getFeedbackId(), request.getFeedbackStatus());
        return AjaxResult.success();
    }
}
