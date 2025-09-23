package org.bigseven.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;

/**
 * @author v185v
 * &#064;date 2025/9/20
 */
@Data
@AllArgsConstructor
@Builder
public class AdminFeedbackRequest {
    Integer feedbackId;
    Integer acceptedByUserId;
    FeedbackStatusEnum feedbackStatus;
}
