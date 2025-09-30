package org.bigseven.controller;

import jakarta.validation.Valid;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.dto.feedback.GetAllFeedbackRequest;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author v185v
 * &#064;date 2025/9/25
 */
@RestController
@RequestMapping("/api/superadmin/feedback")
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
public class FeedbackSuperAdminController {
    private final FeedbackService feedbackService;
    public FeedbackSuperAdminController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }


    /**
     * 将反馈标记为垃圾信息
     * @param request 包含反馈ID和处理人员ID的请求对象
     * @return 包含处理结果的AjaxResult对象
     */
    @PostMapping("/{id}/mark-spam")
    public AjaxResult<GetAllFeedbackResponse> markAsSpam(@RequestBody @Valid GetAllFeedbackRequest request) {
        Integer feedbackId = feedbackService.processFeedback(request.getFeedbackId(), FeedbackStatusEnum.SPAM_APPROVED);
        GetAllFeedbackResponse response = new GetAllFeedbackResponse();
        response.setFeedbackId(feedbackId);
        return AjaxResult.success(response);
    }
}
