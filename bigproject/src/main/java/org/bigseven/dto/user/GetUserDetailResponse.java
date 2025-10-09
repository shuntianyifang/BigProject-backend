package org.bigseven.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bigseven.constant.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * 用户详情响应数据传输对象
 * 用于封装用户详情信息
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDetailResponse {
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    @JsonProperty("user_phone")
    private String userPhone;

    /**
     * 用户类型
     */
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    @JsonProperty("real_name")
    private String realName;

    /**
     * 是否删除
     */
    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    /**
     * 是否删除
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 最后登录时间
     */
    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;
}
