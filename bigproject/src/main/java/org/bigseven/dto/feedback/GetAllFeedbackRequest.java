package org.bigseven.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;

/**
 * 管理员反馈请求数据传输对象
 * 用于封装管理员对反馈进行查询、处理等操作时的请求参数
 * @author v185v
 * &#064;date 2025/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllFeedbackRequest {
    /**
     * 反馈ID
     */
    private Integer feedbackId;
    
    /**
     * 分页页码，默认为1
     */
    private Integer page = 1;
    
    /**
     * 分页大小，默认为10
     */
    private Integer size = 10;
    
    /**
     * 反馈标题，用于模糊查询
     */
    private String title;
    
    /**
     * 是否紧急反馈
     */
    private Boolean isUrgent;
    
    /**
     * 处理该反馈的管理员ID
     */
    private Integer acceptedByUserId;
    
    /**
     * 学生ID，用于查询特定学生提交的反馈
     * 不直接使用userId是为了在后续FeedbackServiceImpl处理时方便区分
     * 实际上后续储存的仍然是userId，只是命名不同
     */
    private Integer studentId;
    
    /**
     * 管理员ID
     */
    private Integer adminId;
    
    /**
     * 是否匿名提交
     */
    private Boolean isNicked;
    
    /**
     * 反馈类型枚举
     */
    private FeedbackTypeEnum feedbackType;
    
    /**
     * 反馈状态枚举
     */
    private FeedbackStatusEnum feedbackStatus;
    
    /**
     * 查询时间范围的起始时间
     */
    private String fromTime;
    
    /**
     * 查询时间范围的结束时间
     */
    private String toTime;
    
    /**
     * 排序字段，默认为"createdAt"
     */
    private String sortField = "created_at";
    
    /**
     * 排序顺序，默认为"desc"(降序)
     */
    private String sortOrder = "desc";
}