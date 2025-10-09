package org.bigseven.dto.feedback;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

import java.util.List;


/**
 * 发布反馈的请求数据传输对象
 *
 * @author v185v
 * &#064;date 2025/9/17
 */
@Data
@AllArgsConstructor
@Builder
public class PublishFeedbackRequest {
    /**
     * 反馈标题
     */
    @Size(min=1, max=40,message = "标题长度必须在1-40之间")
    private String title;

    /**
     * 反馈内容
     */
    @NotBlank(message = "反馈内容不能为空")
    private String content;

    /**
     * 新增图片URL列表
     */
    @JsonProperty("image_url")
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
