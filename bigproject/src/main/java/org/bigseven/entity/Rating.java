package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author v185v
 * &#064;date 2025/9/17
 */
@TableName(value = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @TableId(type = IdType.AUTO)
    private Integer ratingId;

    @TableField("user_id")
    @JsonProperty("user_id")
    private Integer userId;

    @TableField("target_reply_id")
    @JsonProperty("target_reply_id")
    private Integer targetReplyId;

    private String content;

    private Integer score;

    @TableLogic
    private Boolean deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
