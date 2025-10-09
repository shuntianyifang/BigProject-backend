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
 * 用户反馈信息控制器
 *
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

    /**
     * 更新用户反馈信息
     *
     * @param request 包含更新反馈信息的请求对象，包含以下字段：
     *                - isNicked: 是否匿名发布
     *                - isArgent: 是否紧急反馈
     *                - feedbackType: 反馈类型
     *                - title: 反馈标题
     *                - content: 反馈内容
     *                - imageUrls: 图片URL列表
     * @param userDetails 当前认证用户的信息
     * @param id 反馈记录的唯一标识符
     * @return 操作结果，成功时返回空内容的成功响应
     */
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

    /**
     * 删除用户反馈信息
     *
     * @param id 反馈记录的唯一标识符
     * @param userDetails 当前认证用户信息
     * @return 操作结果，成功时返回空内容的成功响应
     */
    @DeleteMapping("/{id}/delete")
    public AjaxResult<Void> deleteFeedback(@PathVariable Integer id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        feedbackService.deleteFeedback(id, userDetails);
        return  AjaxResult.success();
    }

    /**
     * 获取所有反馈信息列表
     *
     * @param request 包含分页参数的请求对象，包含页码和每页大小
     * @return 包含反馈信息列表和分页信息的统一返回结果
     */
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

    /**
     * 获取反馈详情（没用到）
     *
     * @param id 反馈记录的唯一标识符
     * @return 包含反馈详情的返回结果
     */
    @GetMapping("/{id}")
    public AjaxResult<GetFeedbackDetailResponse> getFeedbackDetail(@PathVariable Integer id) {
        GetFeedbackDetailResponse response = feedbackService.getFeedbackDetail(id);
        return AjaxResult.success(response);
    }

    /**
     * 确认反馈处理
     *
     * @param id 反馈记录的唯一标识符
     * @param userDetails 当前认证用户的信息
     * @return 操作结果，成功时返回空内容的成功响应
     */
    @PostMapping("/{id}/confirm")
    public AjaxResult<Void> confirmFeedback(@PathVariable Integer id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        feedbackService.confirmFeedback(id, userDetails);
        return AjaxResult.success();
    }

}
