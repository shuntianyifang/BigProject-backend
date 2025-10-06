package org.bigseven.dto.adminreply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.dto.rating.RatingVO;
import org.bigseven.dto.user.UserSimpleVO;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReplyVO {
    @JsonProperty("admin_reply_id")
    private Integer adminReplyId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("admin")
    private UserSimpleVO admin;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("update_at")
    private LocalDateTime updatedAt;

    @JsonProperty("rating")
    private RatingVO rating;
}