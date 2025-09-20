package org.bigseven.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;

@Data
@AllArgsConstructor
@Builder
public class AdminFeedbackRequest {
    Integer feedbackId;
    Integer acceptedByUserID;
    FeedbackStatusEnum feedbackStatus;
}
