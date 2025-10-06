package org.bigseven.controller;


import jakarta.validation.Valid;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.admin.ProcessFeedbackRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackAdminService;
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
@RequestMapping("/api/manage/feedback")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
public class FeedbackAdminController {

    private final FeedbackAdminService feedbackAdminService;

    public FeedbackAdminController(FeedbackAdminService feedbackAdminService) {
        this.feedbackAdminService = feedbackAdminService;
    }

    /**
     * 管理员处理反馈，标记反馈状态，并可能做出回复
     *
     * @param id 反馈ID
     * @param request 包含标记反馈所需信息的请求对象，包括反馈ID、反馈状态和管理员回复
     * @param userDetails 当前登录管理员的详细信息
     * @return AjaxResult<Void> 操作结果，成功时返回空数据的成功响应
     */
    @PostMapping("/{id}/process")
    public AjaxResult<Void> processFeedback(@PathVariable("id") Integer id,
                                            @Valid @RequestBody ProcessFeedbackRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 从当前登录管理员中获取user_id，作为处理人ID
        Integer acceptedByUserId = userDetails.getUserId();

        // 调用服务层方法，传递管理员回复信息
        feedbackAdminService.processFeedback(
                id,
                request.getFeedbackStatus(),
                request.getAdminReply(),
                acceptedByUserId
        );

        return AjaxResult.success();
    }

    /**
     * 获取反馈类型枚举（没用到）
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
     * 获取反馈状态枚举（没用到）
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
