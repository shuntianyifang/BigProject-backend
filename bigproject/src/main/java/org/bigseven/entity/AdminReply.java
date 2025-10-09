package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员回复
 * 用于保存管理员对反馈的回复信息
 *
 * @author v185v
 * &#064;date 2025/9/17
 */
@TableName(value = "admin_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReply {

    /**
     * 管理员回复id
     */
    @TableId(type = IdType.AUTO)
    private Integer adminReplyId;

    /**
     * 反馈的目标帖子id
     */
    @TableField("feedback_id")
    private Integer feedbackId;

    /**
     * 回复标题
     */
    private String title;

    /**
     * 管理员回复内容
     */
    private String content;

    /**
     * 管理员用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
