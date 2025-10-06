package org.bigseven.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingVO {
    @JsonProperty("rating_id")
    private Integer ratingId;

    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}