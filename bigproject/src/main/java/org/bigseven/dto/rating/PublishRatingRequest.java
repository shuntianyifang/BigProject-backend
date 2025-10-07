package org.bigseven.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "内容不能为空")
    private String content;

    @Size(max=5,min=1,message = "分数必须在1-5之间")
    private Integer score;
}
