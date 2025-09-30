package org.bigseven.service;

import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.entity.AdminReply;

public interface FeedbackAdminService {

    /**
     * @param id
     * @param feedbackStatus
     * @param adminReply
     * @param acceptedByUserId
     */
    void processFeedback(Integer id, FeedbackStatusEnum feedbackStatus, AdminReply adminReply, Integer acceptedByUserId);

}
