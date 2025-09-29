package org.bigseven.dto.admin;

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
public class AdminFeedbackResponse {

    private Integer id;
    
    /**
     * 分页页码，默认为1
     */
    private Integer page = 1;
    
    /**
     * 分页大小，默认为10
     */
    private Integer size = 10;
    
    /**
     * 反馈标题
     */
    private String title;
    
    /**
     * 是否紧急反馈
     */
    private Boolean isUrgent;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 是否匿名提交
     */
    private Boolean isNicked;
    
    /**
     * 反馈状态枚举
     */
    private FeedbackStatusEnum feedbackStatus;

    /**
     * 学生信息
     */
    private UserSimpleVO student;
    
    /**
     * 管理员信息
     */
    private UserSimpleVO admin;
    
    /**
     * 图片URL列表
     */
    private List<String> imageUrls;
}