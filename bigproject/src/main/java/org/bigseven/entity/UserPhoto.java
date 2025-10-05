package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 202510/5
 */
@TableName(value = "user_photo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPhoto {
    @TableId(type = IdType.AUTO)
    private Integer photoId;

    @TableField("user_id")
    @JsonProperty("user_id")
    private Integer userId;
    /**
     * 存储图片的URL或文件路径
     */
    private String photoUrl;
    /**
     * 图片顺序
     */
    private Integer imageOrder;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}