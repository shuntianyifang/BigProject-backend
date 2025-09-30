package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author v185v
 * &#064;date 2025/9/17
 */
@TableName(value = "admin_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReply {

    @TableId(type = IdType.AUTO)
    private Integer adminReplyId;
    /**
     * 反馈的目标帖子id
     */
    @TableField("feedback_id")
    private Integer feedbackId;

    private String title;

    private String content;
    /**
     * 用userId判断userType来鉴权,非管理员不能发送反馈
     */
    @TableField("user_id")
    private Integer userId;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
