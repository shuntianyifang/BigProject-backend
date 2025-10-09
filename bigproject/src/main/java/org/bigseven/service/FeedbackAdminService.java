package org.bigseven.service;

import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.entity.AdminReply;

/**
 * 管理员对反馈进行处理
 *
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
public interface FeedbackAdminService {

    /**
     *管理员处理用户反馈
     *
     * @param id 反馈ID
     * @param feedbackStatus 反馈状态
     * @param adminReply 管理员回复
     * @param acceptedByUserId 管理员ID
     */
    void processFeedback(Integer id, FeedbackStatusEnum feedbackStatus, AdminReply adminReply, Integer acceptedByUserId);

}
