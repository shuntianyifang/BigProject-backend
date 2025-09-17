package org.bigseven.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.FeedbackMapper;
import org.bigseven.mapper.UserMapper;
import org.bigseven.service.FeedbackService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final UserMapper userMapper;
    @Override
    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content) {
        Feedback post = Feedback.builder()
                .userId(userId)
                .isNicked(isNicked)
                .isArgent(isArgent)
                .feedbackType(feedbackType)
                .title(title)
                .content(content)
                .build();
        feedbackMapper.insert(post);
    }

    @Override
    public void markFeedback(Integer userId,Integer feedbackId,Integer acceptedByUserId, Boolean isAccepted, Boolean isResolved) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        //一个简单的鉴权
        //警告:这并不是一个安全的鉴权方式，因为它信任了前端传的userId
        //后期应该改为在Controller 或 Service 方法上使用 @PreAuthorize 注解
        User operatorUser = userMapper.selectById(acceptedByUserId);
        if (feedback != null &&operatorUser != null && operatorUser.getUserType()!= UserTypeEnum.STUDENT) {
           feedback.setIsAccepted(isAccepted);
           feedback.setIsResolved(isResolved);
           feedbackMapper.updateById(feedback);
        }
        else {
            //需要做错误处理
            return;
        }
    }

    @Override
    public void deleteFeedback(Integer userId,Integer feedbackId) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            if (feedback.getUserId().equals(userId)) {
                throw new ApiException(ExceptionEnum.INVALID_PARAMETER);
            }
            feedbackMapper.deleteById(feedbackId);
        } else {
            throw new ApiException(ExceptionEnum.RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public void updateFeedback(Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            feedback.setFeedbackType(feedbackType);
            feedback.setTitle(title);
            feedback.setContent(content);
            feedbackMapper.updateById(feedback);
        }
        else {
            //需要做错误处理
            return;
        }

    }
}
