package org.bigseven.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bigseven.constant.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * 反馈响应数据传输对象
 * 用于封装返回给用户的反馈信息
 *
 * @author v185v
 * &#064;date 2025/9/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserResponse {

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 用户名
     */
    @JsonProperty("username")
    private String username;

    /**
     * 邮箱
     */
    @JsonProperty("email")
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
    @JsonProperty("nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @JsonProperty("real_name")
    private String realName;

    /**
     * 是否删除
     */
    @JsonProperty("deleted")
    private Boolean deleted;

    /**
     * 反馈创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * 反馈更改时间
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 反馈处理时间
     */
    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 用户头像URL
     */
    @JsonProperty("profile_photo")
    private String profilePhoto;
}