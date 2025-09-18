package org.bigseven.service;

import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.util.List;

public interface FeedbackService {

    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls);
    //需要鉴权
    public void markFeedback(Integer userId, Integer feedbackId, Integer acceptedByUserID, FeedbackStatusEnum feedbackStatus);
    //需要鉴权
    public void deleteFeedback(Integer userId,Integer feedbackId);

    public void updateFeedback(Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content);
}
