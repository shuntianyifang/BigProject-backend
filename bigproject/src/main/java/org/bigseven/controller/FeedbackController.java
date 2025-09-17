package org.bigseven.controller;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.bigseven.dto.PublishFeedbackRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Resource
    private FeedbackService feedbackService;
    @PostMapping
    public AjaxResult<Void> publishPost(@Valid @RequestBody PublishFeedbackRequest request){
        feedbackService.publishFeedback(request.getUserId(), request.getIsNicked(), request.getIsArgent(), request.getPostType(), request.getTitle(), request.getContent());
        return AjaxResult.success();
    }
    /**
     * 管理员标记帖子
     * Description:我不确定这样鉴权对不对，还需要再查一下Spring Security注解的用法
     */
    @PostMapping("/mark")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public AjaxResult<Void> markPost(@Valid @RequestBody PublishFeedbackRequest request){
        feedbackService.markFeedback(request.getUserId(), request.getPostId(), request.getAcceptedByUserId(), request.getIsAccepted(), request.getIsResolved());
        return AjaxResult.success();
    }
}
