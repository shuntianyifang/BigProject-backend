package org.bigseven.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 评分请求数据传输对象
 *
 * @author shuntianyifang
 * &#064;date 2025/10/5
 */
@Data
@AllArgsConstructor
@Builder
public class PublishRatingRequest {

    /**
     * 管理员回复ID
     */
    @JsonProperty("admin_reply_id")
    private Integer adminReplyId;

    /**
     * 反馈ID
     */
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    /**
     * 评分内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 评分分数
     */
    @Size(max=5,min=1,message = "分数必须在1-5之间")
    private Integer score;
}
