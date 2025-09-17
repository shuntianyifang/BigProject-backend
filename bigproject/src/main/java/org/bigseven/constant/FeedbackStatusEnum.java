package org.bigseven.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackStatusEnum {
    PENDING(0, "待处理"),
    PROCESSING(1, "处理中"),
    RESOLVED(2, "已解决"),
    SPAM_PENDING(3, "待审核垃圾信息"),
    SPAM_APPROVED(4, "已确认垃圾信息");
    private final Integer statusCode;
    private final String statusDescription;
}
