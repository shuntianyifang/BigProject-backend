package org.bigseven.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class PublishRatingRequest {

    @JsonProperty("admin_reply_id")
    private Integer adminReplyId;

    @JsonProperty("feedback_id")
    private Integer feedbackId;

    private String content;

    private Integer score;
}
