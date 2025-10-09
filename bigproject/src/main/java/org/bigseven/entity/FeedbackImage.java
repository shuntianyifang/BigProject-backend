package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 这是反馈图片实体类
 *
 * @author v185v
 * &#064;date 2025/9/18
 */
@TableName(value = "feedback_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackImage {
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Integer imageId;

    /**
     * 反馈ID
     */
    @TableField("feedback_id")
    @JsonProperty("feedback_id")
    private Integer feedbackId;
    /**
     * 存储图片的URL或文件路径
     */
    @TableField("image_url")
    @JsonProperty("image_url")
    private String imageUrl;
    /**
     * 图片顺序
     */
    @TableField("image_order")
    @JsonProperty("image_order")
    private Integer imageOrder;

    /**
     * 图片创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
