package org.bigseven.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Size;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.feedback.GetAllFeedbackRequest;
import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.security.CustomUserDetails;

import java.util.List;

/**
 * @author v185v
 * &#064;date  2025/9/17
 */

public interface FeedbackService {

    /**
     * 发布新的用户反馈
     * 创建带有指定类型、标题和内容的反馈，支持图片附件和匿名/加急选项
     *
     * @param userId       发布者的用户ID
     * @param isNicked     是否匿名发布
     * @param isUrgent     是否为加急反馈
     * @param feedbackType 反馈类型枚举
     * @param title        反馈标题
     * @param content      反馈详细内容
     * @param imageUrls    反馈相关的图片URL列表
     */
    void publishFeedback(Integer userId, Boolean isNicked, Boolean isUrgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls);

    /**
     * 删除指定用户的反馈
     * 根据用户ID和反馈ID验证权限后执行物理/逻辑删除
     * @param id 反馈的唯一标识ID
     * @param userDetails 执行删除操作的用户
     */
    void deleteFeedback(Integer id, CustomUserDetails userDetails);

    /**
     * 管理员查看所有反馈（分页+条件查询）
     *
     * @param request 管理员反馈查询请求，包含分页参数和筛选条件
     * @return 分页的反馈响应列表，包含反馈详情和分页信息
     */
    Page<GetAllFeedbackResponse> getAllFeedbacks(GetAllFeedbackRequest request);

    /**
     * 根据ID获取反馈详情
     *
     * @param id 反馈的唯一标识ID
     * @return 反馈的详细信息响应对象
     */
    GetFeedbackDetailResponse getFeedbackDetail(Integer id);

    /**
     * 更新反馈信息
     *
     * @param id 反馈的唯一标识ID
     * @param userDetails 执行更新操作的用户
     * @param isNicked 是否匿名发布
     * @param isUrgent 是否为加急反馈
     * @param feedbackType 反馈类型枚举
     * @param title 反馈标题
     * @param content 反馈详细内容
     * @param imageUrls 反馈相关的图片URL列表
     */
    void updateFeedback(Integer id, CustomUserDetails userDetails, Boolean isNicked, Boolean isUrgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls);

    /**
     * 确认反馈
     *
     * @param id 反馈的唯一标识ID
     * @param userDetails 执行确认操作的用户
     */
    void confirmFeedback(Integer id, CustomUserDetails userDetails);
}
