package org.bigseven.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 反馈状态枚举
 * 定义用户反馈的处理状态及其流转
 *
 * @author v185v
 * &#064;date 2025/9/18
 */

@Getter
@AllArgsConstructor
public enum FeedbackStatusEnum {

    /**
     * 新提交的反馈，等待管理员处理
     */
    PENDING(0, "待处理"),
    /**
     * 反馈已被管理员接收并正在处理中
     */
    PROCESSING(1, "处理中"),
    /**
     * 反馈已处理完成并给出解决方案
     */
    RESOLVED(2, "已解决"),
    /**
     * 反馈被标记为疑似垃圾信息，等待审核确认
     */
    SPAM_PENDING(3, "待审核垃圾信息"),
    /**
     * 反馈已被确认为垃圾信息，将不再处理
     */
    SPAM_APPROVED(4, "已确认垃圾信息");

    /**
     * 状态编码，用于数据库存储和程序逻辑处理
     * 采用数字类型便于扩展和比较
     */
    @EnumValue
    private final Integer statusCode;
    /**
     * 状态描述，用于界面展示和日志记录
     * 提供用户友好的状态说明
     */
    private final String statusDescription;
}
