package org.bigseven.service;

import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.util.List;

public interface FeedbackService {

    void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls);

    Integer markFeedback(Integer feedbackId, Integer acceptedByUserId, FeedbackStatusEnum feedbackStatus);

    void deleteFeedback(Integer userId, Integer feedbackId);

    void updateFeedback(Integer userId, Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content);
}
