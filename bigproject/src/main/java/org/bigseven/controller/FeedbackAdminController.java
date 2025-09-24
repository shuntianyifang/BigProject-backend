package org.bigseven.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.admin.AdminFeedbackRequest;
import org.bigseven.dto.admin.AdminFeedbackResponse;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public AjaxResult<AdminFeedbackResponse> acceptFeedback(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.PROCESSING);
        AdminFeedbackResponse response = new AdminFeedbackResponse();
        response.setId(id);
        return AjaxResult.success(response);
    }

    /**
     * 将反馈标记为垃圾信息
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/mark-spam")
    public AjaxResult<AdminFeedbackResponse> markAsSpam(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.SPAM_PENDING);
        AdminFeedbackResponse response = new AdminFeedbackResponse();
        response.setId(id);
        return AjaxResult.success(response);
    }

    /**
     * 将反馈标记为已解决
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/mark-resolved")
    public AjaxResult<AdminFeedbackResponse> markAsResolved(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.RESOLVED);
        AdminFeedbackResponse response = new AdminFeedbackResponse();
        response.setId(id);
        return AjaxResult.success(response);
    }

    /**
     * 管理员查看所有反馈（分页+条件查询）
     * @param request 包含分页和查询条件的请求对象
     * @return 包含分页反馈列表的AjaxResult对象
     */
    @GetMapping
    public AjaxResult<Page<AdminFeedbackResponse>> getAllFeedbacks(@Valid AdminFeedbackRequest request) {
        Page<AdminFeedbackResponse> feedbacks = feedbackService.getAllFeedbacks(request);
        return AjaxResult.success(feedbacks);
    }

    /**
     * 查看反馈详情
     * @param id 反馈ID
     * @return 包含反馈详情的AjaxResult对象
     */
    @GetMapping("/{id}")
    public AjaxResult<AdminFeedbackResponse> getFeedbackDetail(@PathVariable Integer id) {
        AdminFeedbackResponse feedback = feedbackService.getFeedbackDetail(id);
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
