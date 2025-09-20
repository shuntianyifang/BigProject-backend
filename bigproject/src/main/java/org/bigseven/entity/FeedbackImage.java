package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName(value = "feedback_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackImage {
    @TableId(type = IdType.AUTO)
    private Integer imageId;

    @TableField("feedback_id")
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    // 存储图片的URL或文件路径
    private String imageUrl;

    // 图片顺序
    private Integer imageOrder;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
