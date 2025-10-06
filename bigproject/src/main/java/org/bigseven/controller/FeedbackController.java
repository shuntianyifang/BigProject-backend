package org.bigseven.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.bigseven.dto.base.BaseListResponse;
import org.bigseven.dto.feedback.*;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
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

    @PostMapping("/{id}")
    public AjaxResult<Void> updateFeedback(@Valid @RequestBody UpdateFeedbackRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Integer id) {
        feedbackService.updateFeedback(id, userDetails,
                request.getIsNicked(),
                request.getIsUrgent(),
                request.getFeedbackType(),
                request.getTitle(),
                request.getContent(),
                request.getImageUrls()
        );
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}/delete")
    public AjaxResult<Void> deleteFeedback(@PathVariable Integer id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        feedbackService.deleteFeedback(id, userDetails);
        return  AjaxResult.success();
    }

    @GetMapping
    public AjaxResult<BaseListResponse<GetAllFeedbackResponse>> getAllFeedback(GetAllFeedbackRequest request) {

        Page<GetAllFeedbackResponse> pageResult = feedbackService.getAllFeedbacks(request);

        BaseListResponse<GetAllFeedbackResponse> response = BaseListResponse.<GetAllFeedbackResponse>builder()
                .list(pageResult.getRecords())
                .total((int) pageResult.getTotal())
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages((int) pageResult.getPages())
                .build();

        return AjaxResult.success(response);
    }

    @GetMapping("/{id}")
    public AjaxResult<GetFeedbackDetailResponse> getFeedbackDetail(@PathVariable Integer id) {
        GetFeedbackDetailResponse response = feedbackService.getFeedbackDetail(id);
        return AjaxResult.success(response);
    }

    @PostMapping("/{id}/confirm")
    public AjaxResult<Void> confirmFeedback(@PathVariable Integer id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        feedbackService.confirmFeedback(id, userDetails);
        return AjaxResult.success();
    }

}
