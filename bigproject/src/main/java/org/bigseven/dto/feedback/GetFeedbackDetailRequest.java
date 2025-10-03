package org.bigseven.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFeedbackDetailRequest {
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
