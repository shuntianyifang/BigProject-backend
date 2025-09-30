package org.bigseven.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.feedback.GetAllFeedbackRequest;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.PublishFeedbackRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author v185v
 * &#064;date 2025/9/20
 */
@RestController
@RequestMapping("/api/admin/feedback")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class FeedbackAdminController {

    private final FeedbackService feedbackService;

    public FeedbackAdminController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 接受反馈处理请求
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/accept")
    public AjaxResult<GetAllFeedbackResponse> acceptFeedback(@RequestBody @Valid GetAllFeedbackRequest request) {
        Integer feedbackId = feedbackService.processFeedback(request.getFeedbackId(), FeedbackStatusEnum.PROCESSING);
        GetAllFeedbackResponse response = new GetAllFeedbackResponse();
        response.setFeedbackId(feedbackId);
        return AjaxResult.success(response);
    }

    /**
     * 管理员处理反馈，标记反馈状态，可能做出回复
     *
     * @param request 包含标记反馈所需信息的请求对象，包括反馈ID和反馈状态
     * @param userDetails 当前登录管理员的详细信息
     * @return AjaxResult<Void> 操作结果，成功时返回空数据的成功响应
     */
    @PostMapping("/{id}/process")
    public AjaxResult<Void> processFeedback(@Valid @RequestBody PublishFeedbackRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 从当前登录管理员中获取user_id，作为处理人ID
        Integer acceptedByUserId = userDetails.getUserId();
        feedbackService.processFeedback(request.getFeedbackId(), request.getFeedbackStatus());
        return AjaxResult.success();
    }

    /**
     * 将反馈标记为待确认垃圾信息
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/mark-spam-pending")
    public AjaxResult<GetAllFeedbackResponse> markAsSpamPeding(@RequestBody @Valid GetAllFeedbackRequest request) {
        Integer feedbackId = feedbackService.processFeedback(request.getFeedbackId(), FeedbackStatusEnum.SPAM_PENDING);
        GetAllFeedbackResponse response = new GetAllFeedbackResponse();
        response.setFeedbackId(feedbackId);
        return AjaxResult.success(response);
    }

    /**
     * 将反馈标记为已解决
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/mark-resolved")
    public AjaxResult<GetAllFeedbackResponse> markAsResolved(@RequestBody @Valid GetAllFeedbackRequest request) {
        Integer feedbackId = feedbackService.processFeedback(request.getFeedbackId(), FeedbackStatusEnum.RESOLVED);
        GetAllFeedbackResponse response = new GetAllFeedbackResponse();
        response.setFeedbackId(feedbackId);
        return AjaxResult.success(response);
    }

    /**
     * 查看反馈详情
     * @param id 反馈ID
     * @return 包含反馈详情的AjaxResult对象
     */
    @GetMapping("/{id}")
    public AjaxResult<GetAllFeedbackResponse> getFeedbackDetail(@PathVariable Integer id) {
        GetAllFeedbackResponse feedback = feedbackService.getFeedbackDetail(id);
        return AjaxResult.success(feedback);
    }

    /**
     * 获取反馈类型枚举
     * @return 包含所有反馈类型的AjaxResult对象
     */
    @GetMapping("/types")
    public AjaxResult<List<String>> getFeedbackTypes() {
        //从枚举中获取所有反馈类型
        List<String> types = Arrays.stream(FeedbackTypeEnum.values())
                .map(FeedbackTypeEnum::getDisplayName)
                .toList();
        return AjaxResult.success(types);
    }

    /**
     * 获取反馈状态枚举
     * @return 包含所有反馈状态的AjaxResult对象
     */
    @GetMapping("/statuses")
    public AjaxResult<List<String>> getFeedbackStatuses() {
        List<String> statuses =  Arrays.stream(FeedbackStatusEnum.values())
                .map(Enum::name)
                .toList();
        return AjaxResult.success(statuses);
    }
}
