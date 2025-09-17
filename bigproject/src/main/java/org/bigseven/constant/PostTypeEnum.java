package org.bigseven.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostTypeEnum {
    // --- 设施保修 ---
    DORMITORY_REPAIR("宿舍设施报修"),
    ACADEMIC_FACILITIES("教学设施报修"),
    PUBLIC_FACILITIES("公共设施报修"),

    // --- 校园后勤服务 ---
    NETWORK_IT_SERVICES("校园网服务"),
    CANTEEN_DINING("食堂餐饮问题"),
    CAMPUS_ENVIRONMENT("校园环境问题"),
    SECURITY_SAFETY("校园安全问题"),

    // --- 意见建议与其他 ---
    SUGGESTIONS_OPINIONS("意见与建议"),
    OTHER_ISSUES("其他");

    private final String displayName;
}
