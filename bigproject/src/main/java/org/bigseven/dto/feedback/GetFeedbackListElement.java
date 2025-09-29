package org.bigseven.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/29
 */
@Data
@Builder
public class GetFeedbackListElement {
    private Integer feedbackId;

    private String title;

    private String content;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
