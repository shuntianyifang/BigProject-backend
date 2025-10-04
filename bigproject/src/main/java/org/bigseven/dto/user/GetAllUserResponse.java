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

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("user_phone")
    private String userPhone;

    @JsonProperty("user_type")
    private UserTypeEnum userType;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("real_name")
    private String realName;

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