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
 * 管理员回复VO
 *
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReplyVO {
    /**
     * 管理员回复ID
     */
    @JsonProperty("admin_reply_id")
    private Integer adminReplyId;

    /**
     * 管理员回复内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 管理员回复创建者
     */
    @JsonProperty("admin")
    private UserSimpleVO admin;

    /**
     * 管理员回复创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 管理员回复更新时间
     */
    @JsonProperty("update_at")
    private LocalDateTime updatedAt;

    /**
     * 关联的评分信息
     */
    @JsonProperty("rating")
    private RatingVO rating;
}