package org.bigseven.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
public class PublishFeedbackRequest {

    private Integer feedbackId;

    private String title;

    private String content;

    //帖子类型
    private FeedbackTypeEnum feedbackType;

    //是否匿名
    private Boolean isNicked;

    //是否紧急
    private Boolean isArgent;

    //发布帖子的用户的id
    private Integer userId;

    //接单的管理员id
    private Integer acceptedByUserId;

    //帖子状态
    private FeedbackStatusEnum feedbackStatus;

    private Integer viewCount;

    private Boolean deleted;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
