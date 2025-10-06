package org.bigseven.dto.feedback;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.adminreply.AdminReplyVO;
import org.bigseven.dto.user.UserSimpleVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/2
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedbackDetailResponse {

    @JsonProperty("feedback_id")
    private Integer feedbackId;
    /**
     * 反馈标题
     */
    @JsonProperty("title")
    private String title;

    /**
     * 反馈内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 是否紧急反馈
     */
    @JsonProperty("is_urgent")
    private Boolean isUrgent;

    /**
     * 是否匿名提交
     */
    @JsonProperty("is_nicked")
    private Boolean isNicked;

    /**
     * 学生是否确认收到管理员回复
     */
    @JsonProperty("is_confirmed")
    private Boolean isConfirmed;


    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 接单的管理员id
     */
    @JsonProperty("accepted_by_user_id")
    private Integer acceptedByUserId;

    @JsonProperty("view_count")
    private Integer viewCount;

    /**
     * 帖子类型
     */
    @JsonProperty("feedback_type")
    private FeedbackTypeEnum feedbackType;

    /**
     * 反馈状态枚举
     */
    @JsonProperty("feedback_status")
    private FeedbackStatusEnum feedbackStatus;

    /**
     * 学生信息
     */
    @JsonProperty("student")
    private UserSimpleVO student;

    /**
     * 管理员信息
     */
    @JsonProperty("admin")
    private UserSimpleVO admin;

    /**
     * 管理员回复信息
     */
    @JsonProperty("admin_reply")
    private List<AdminReplyVO> adminReply;

    /**
     * 反馈创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 反馈更改时间
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 反馈处理时间
     */
    @JsonProperty("processed_at")
    private LocalDateTime processedAt;

    /**
     * 图片URL列表
     */
    @JsonProperty("image_urls")
    private List<String> imageUrls;
}
