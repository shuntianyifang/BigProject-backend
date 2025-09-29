package org.bigseven.constant;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bigseven.exception.ApiException;

/**
 * 反馈类型枚举
 * 定义系统中不同类型的反馈
 * 反馈类型：
 * 10XX:意见建议与其他
 * 20XX:设施保修
 * 30XX:校园后勤服务
 *
 * @author v185v
 * &#064;date 2025/9/17
 */

@AllArgsConstructor
@Getter
public enum FeedbackTypeEnum {

    // --- 意见建议与其他 ---
    SUGGESTIONS_OPINIONS(1000,"意见与建议"),
    OTHER_ISSUES(1001,"其他"),

    // --- 设施保修 ---
    DORMITORY_REPAIR(2000,"宿舍设施报修"),
    ACADEMIC_FACILITIES(2001,"教学设施报修"),
    PUBLIC_FACILITIES(2002,"公共设施报修"),

    // --- 校园后勤服务 ---
    NETWORK_IT_SERVICES(3000,"校园网服务"),
    CANTEEN_DINING(3001,"食堂餐饮问题"),
    CAMPUS_ENVIRONMENT(3002,"校园环境问题"),
    SECURITY_SAFETY(3003,"校园安全问题");

    /**
     * 反馈类型编码，用于数据库存储和程序逻辑判断
     */
    @EnumValue
    @JsonValue
    private final Integer FeedbackTypeCode;
    /**
     * 反馈类型显示名称，用于界面展示
     */
    private final String displayName;

    @JsonCreator
    public static FeedbackTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (FeedbackTypeEnum type : FeedbackTypeEnum.values()) {
            if (type.FeedbackTypeCode.equals(code)) {
                return type;
            }
        }
        throw new ApiException(ExceptionEnum.ILLEGAL_FEEDBACK_TYPE);
    }
}
