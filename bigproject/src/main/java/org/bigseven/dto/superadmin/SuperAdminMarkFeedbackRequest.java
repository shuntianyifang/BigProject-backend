package org.bigseven.dto.superadmin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;

@Data
@AllArgsConstructor
@Builder
public class SuperAdminMarkFeedbackRequest {
    /**
     * 反馈ID
     */
    private Integer feedbackId;

    /**
     * 反馈状态枚举
     */
    private FeedbackStatusEnum feedbackStatus;
}
