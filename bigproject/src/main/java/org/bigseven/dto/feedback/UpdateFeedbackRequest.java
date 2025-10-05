package org.bigseven.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UpdateFeedbackRequest {
    @Size(min=1, max=40,message = "标题长度必须在1-40之间")
    private String title;

    private String content;

    /**
     * 新增图片URL列表
     */
    private List<String> imageUrls;
    /**
     * 帖子类型
     */
    @JsonProperty("feedback_type")
    private FeedbackTypeEnum feedbackType;
    /**
     * 是否匿名
     */
    @JsonProperty("is_nicked")
    private Boolean isNicked;
    /**
     * 是否紧急
     */
    @JsonProperty("is_urgent")
    private Boolean isUrgent;
}
