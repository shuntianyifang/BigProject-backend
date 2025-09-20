package org.bigseven.controller;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.dto.AdminFeedbackRequest;
import org.bigseven.dto.AdminFeedbackResponse;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/feedback")
public class FeedbackAdminController {
    @Resource
    private FeedbackService feedbackService;
    @PostMapping("/{id}/accept")
    public AjaxResult<AdminFeedbackResponse> acceptFeedback(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.PROCESSING);
        return AjaxResult.success(new AdminFeedbackResponse(id));
    }


    @PostMapping("/{id}/mark-spam")
    public AjaxResult<AdminFeedbackResponse> markAsSpam(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.SPAM_PENDING);
        return AjaxResult.success(new AdminFeedbackResponse(id));
    }

    @PostMapping("/{id}/mark-resolved")
    public AjaxResult<AdminFeedbackResponse> markAsResolved(@RequestBody @Valid AdminFeedbackRequest request) {
        Integer id = feedbackService.markFeedback(request.getFeedbackId(), request.getAcceptedByUserId(), FeedbackStatusEnum.RESOLVED);
        return AjaxResult.success(new AdminFeedbackResponse(id));
    }

}
