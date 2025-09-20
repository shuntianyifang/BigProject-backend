package org.bigseven.service;

import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.util.List;

public interface FeedbackService {

    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls);

    public Integer markFeedback(Integer feedbackId, Integer acceptedByUserId, FeedbackStatusEnum feedbackStatus);

    public void deleteFeedback(Integer userId,Integer feedbackId);

    public void updateFeedback(Integer userId,Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content);
}
