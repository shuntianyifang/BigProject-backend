package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.constant.FeedbackTypeEnum;

import java.time.LocalDateTime;

/**
 * Description:这是学生问题帖子实体类,与管理员反馈不是同一个实体类
 * @TableName feedback
 */
@TableName(value = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @TableId(type = IdType.AUTO)
    @TableField("feedback_id")
    private Integer feedbackId;

    private String title;

    private String content;

    @TableField("user_id")
    private Integer userId;

    @TableField("view_count")
    private Integer viewCount;

    //接单的管理员id
    @TableField("accepted_by_user_id")
    private Integer acceptedByUserId;

    //帖子类型
    @TableField("feedback_type")
    private FeedbackTypeEnum feedbackType;

    //是否匿名
    @TableField("is_nicked")
    private Boolean isNicked;

    //是否紧急
    @TableField("is_argent")
    private Boolean isArgent;

    //是否已经由管理员接单
    @TableField("is_accepted")
    private Boolean isAccepted;

    //是否已经反馈完成
    @TableField("is_resolved")
    private Boolean isResolved;

    //是否为垃圾信息
    @TableField("is_trash")
    private Boolean isTrash;

    @TableLogic
    private Boolean deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
