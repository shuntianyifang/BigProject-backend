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
    public AjaxResult<Void> publishFeedback(@Valid @RequestBody PublishFeedbackRequest request){
        feedbackService.publishFeedback(request.getUserId(),
                                        request.getIsNicked(),
                                        request.getIsArgent(),
                                        request.getFeedbackType(),
                                        request.getTitle(),
                                        request.getContent(),
                                        request.getImageUrls()
                                        );
        return AjaxResult.success();
    }


    /**
     * 管理员标记帖子状态
     * 我不确定这样鉴权对不对，还需要再查一下Spring Security注解的用法
     * @param request 包含标记反馈所需信息的请求对象，包括用户ID、反馈ID、处理人ID和反馈状态
     * @return AjaxResult<Void> 操作结果，成功时返回空数据的成功响应
     */
    @PostMapping("/mark")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public AjaxResult<Void> markFeedback(@Valid @RequestBody PublishFeedbackRequest request){
        feedbackService.markFeedback(request.getFeedbackId(),
                                    request.getAcceptedByUserId(),
                                    request.getFeedbackStatus()
                                    );
        return AjaxResult.success();
    }
}
