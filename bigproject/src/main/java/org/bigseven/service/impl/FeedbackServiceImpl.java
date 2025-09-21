package org.bigseven.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.config.FeedbackConfig;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.FeedbackImage;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.FeedbackImageMapper;
import org.bigseven.mapper.FeedbackMapper;
import org.bigseven.mapper.UserMapper;
import org.bigseven.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author v185v
 * &#064;date  2025/9/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final UserMapper userMapper;
    private final FeedbackImageMapper feedbackImageMapper;
    private final FeedbackConfig feedbackConfig;

    /**
     * 发布用户反馈信息
     *
     * @param userId 用户ID
     * @param isNicked 是否匿名发布
     * @param isArgent 是否紧急反馈
     * @param feedbackType 反馈类型枚举
     * @param title 反馈标题
     * @param content 反馈内容
     * @param imageUrls 反馈图片URL列表，最多保存前9张图片
     */
    @Override
    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls) {
        Feedback post = Feedback.builder()
                .userId(userId)
                .isNicked(isNicked)
                .isArgent(isArgent)
                .feedbackType(feedbackType)
                .title(title)
                .content(content)
                .build();
        feedbackMapper.insert(post);

        /// 保存图片信息
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < Math.min(imageUrls.size(), feedbackConfig.getMaxImages()); i++) {
                FeedbackImage image = FeedbackImage.builder()
                        .feedbackId(post.getFeedbackId())
                        .imageUrl(imageUrls.get(i))
                        .imageOrder(i)
                        .build();
                feedbackImageMapper.insert(image);
            }
        }
    }

    /**
     * 管理员标记反馈状态
     * @param feedbackId 反馈ID
     * @param acceptedByUserId 处理人员ID
     * @param feedbackStatus 反馈状态枚举值
     */
    @Override
    public Integer markFeedback(Integer feedbackId,Integer acceptedByUserId, FeedbackStatusEnum feedbackStatus) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        //一个简单的鉴权
        //警告:这并不是一个安全的鉴权方式，因为它信任了前端传的userId
        //后期应该改为在Controller 或 Service 方法上使用 @PreAuthorize 注解
        User operatorUser = userMapper.selectById(acceptedByUserId);
        if (feedback != null &&operatorUser != null && operatorUser.getUserType()!= UserTypeEnum.STUDENT) {
           feedback.setFeedbackStatus(feedbackStatus);
           feedbackMapper.updateById(feedback);
        }
        else {
            //需要做错误处理
            return -1;
        }
        return operatorUser.getUserId();
    }

    /**
     * 删除反馈信息
     * @param userId 用户ID
     * @param feedbackId 反馈ID
     */
    @Override
    public void deleteFeedback(Integer userId,Integer feedbackId) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            /// 验证用户权限，不能删除其他用户的反馈
            if (feedback.getUserId().equals(userId)) {
                throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
            }
            /// 执行删除操作
            feedbackMapper.deleteById(feedbackId);
        } else {
            throw new ApiException(ExceptionEnum.RESOURCE_NOT_FOUND);
        }
    }

    /**
     * 普通User更新反馈信息
     * @param feedbackId 反馈ID
     * @param feedbackType 反馈类型
     * @param title 反馈标题
     * @param content 反馈内容
     */
    @Override
    public void updateFeedback(Integer userId,Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            if(feedback.getUserId().equals(userId)){
                feedback.setFeedbackType(feedbackType);
                feedback.setTitle(title);
                feedback.setContent(content);
                feedbackMapper.updateById(feedback);
            }
            else {
                throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
            }
        }
        else {
            //需要做错误处理
            return;
        }

    }
}
