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

    private String content;

    private Integer score;
}
