package org.bigseven.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 评分信息数据传输对象
 *
 * @author shuntianyifang
 * &#064;date 2025/10/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingVO {
    /**
     * 评分ID
     */
    @JsonProperty("rating_id")
    private Integer ratingId;

    /**
     * 评分对应的反馈ID
     */
    @JsonProperty("feedback_id")
    private Integer feedbackId;

    /**
     * 评分内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 评分分数
     */
    @JsonProperty("score")
    private Integer score;

    /**
     * 评分创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 评分更新时间
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}