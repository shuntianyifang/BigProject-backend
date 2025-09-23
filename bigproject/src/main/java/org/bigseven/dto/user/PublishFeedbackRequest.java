package org.bigseven.dto.user;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author v185v
 * &#064;date 2025/9/17
 */
@Data
@AllArgsConstructor
@Builder
public class PublishFeedbackRequest {

    private Integer feedbackId;

    @Size(min=1, max=40,message = "标题长度必须在1-40之间")
    private String title;

    private String content;

    /**
     * 新增图片URL列表
     */
    private List<String> imageUrls;
    /**
     * 帖子类型
     */
    private FeedbackTypeEnum feedbackType;
    /**
     * 是否匿名
     */
    private Boolean isNicked;
    /**
     * 是否紧急
     */
    private Boolean isArgent;
    /**
     * 发布帖子的用户的id
     */
    private Integer userId;
    /**
     * 接单的管理员id
     */
    private Integer acceptedByUserId;
    /**
     * 帖子状态
     */
    private FeedbackStatusEnum feedbackStatus;

    private Integer viewCount;

    private Boolean deleted;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
