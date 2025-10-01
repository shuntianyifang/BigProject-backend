package org.bigseven.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.entity.AdminReply;

/**
 * 处理反馈的请求对象
 *
 * @author shuntianyifang
 * &#064;date
 */
@Setter
@Getter
public class ProcessFeedbackRequest {

    @NotNull(message = "反馈状态不能为空")
    @JsonProperty("feedback_status")
    private FeedbackStatusEnum feedbackStatus;

    @JsonProperty("admin_reply")
    private AdminReply adminReply;

}
