package org.bigseven.service;

import org.bigseven.constant.FeedbackTypeEnum;

public interface FeedbackService {

    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum postType, String title, String content);
    //需要鉴权
    public void markFeedback(Integer userId,Integer postId, Integer acceptedByUserID,Boolean isAccepted, Boolean isResolved);
    //需要鉴权
    public void deleteFeedback(Integer userId,Integer postId);

    public void updateFeedback(Integer postId, FeedbackTypeEnum postType, String title, String content);
}
