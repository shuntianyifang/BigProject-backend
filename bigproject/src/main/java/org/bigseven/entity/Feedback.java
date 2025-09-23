package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.time.LocalDateTime;

/**
 * Description:这是学生问题帖子实体类,与管理员反馈不是同一个实体类
 * &#064;TableName  feedback
 *
 * @author v185v
 * &#064;date 2025/9/17
 */
@TableName(value = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @TableId(type = IdType.AUTO)
    @JsonProperty("feedback_id")
    private Integer feedbackId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;

    @TableField("user_id")
    @JsonProperty("user_id")
    private Integer userId;

    @TableField("view_count")
    @JsonProperty("view_count")
    private Integer viewCount;
    /**
     * 接单的管理员id
     */
    @TableField("accepted_by_user_id")
    @JsonProperty("accepted_by_user_id")
    private Integer acceptedByUserId;
    /**
     * 帖子类型
     */
    @TableField("feedback_type")
    @JsonProperty("feedback_type")
    private FeedbackTypeEnum feedbackType;
    /**
     * 是否匿名
     */
    @TableField("is_nicked")
    @JsonProperty("is_nicked")
    private Boolean isNicked;
    /**
     * 是否紧急
     */
    @TableField("is_urgent")
    @JsonProperty("is_urgent")
    private Boolean isUrgent;
    /**
     * 帖子状态
     */
    @TableField("feedback_status")
    @JsonProperty("feedback_status")
    private FeedbackStatusEnum feedbackStatus;

    @TableLogic
    private Boolean deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
