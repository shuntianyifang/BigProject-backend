package org.bigseven.dto.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.dto.user.UserSimpleVO;

import java.util.List;

/**
 * 管理员反馈响应数据传输对象
 * 用于封装返回给管理员的反馈信息
 * @author v185v
 * &#064;date 2025/9/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllFeedbackResponse {

    @JsonProperty("feedback_id")
    private Integer feedbackId;
    /**
     * 反馈标题
     */
    @JsonProperty("title")
    private String title;
    
    /**
     * 是否紧急反馈
     */
    @JsonProperty("is_urgent")
    private Boolean isUrgent;
    
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;
    
    /**
     * 是否匿名提交
     */
    @JsonProperty("is_nicked")
    private Boolean isNicked;
    
    /**
     * 反馈状态枚举
     */
    @JsonProperty("feedback_status")
    private FeedbackStatusEnum feedbackStatus;

    /**
     * 学生信息
     */
    @JsonProperty("student")
    private UserSimpleVO student;
    
    /**
     * 管理员信息
     */
    @JsonProperty("admin")
    private UserSimpleVO admin;
    
    /**
     * 图片URL列表
     */
    @JsonProperty("image_urls")
    private List<String> imageUrls;
}