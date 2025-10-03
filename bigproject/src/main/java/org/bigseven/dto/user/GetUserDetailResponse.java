package org.bigseven.dto.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bigseven.constant.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDetailResponse {
    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    private String password;

    private String email;

    @JsonProperty("user_phone")
    private String userPhone;

    @JsonProperty("user_type")
    private UserTypeEnum userType;

    private String nickname;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("create_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;
}
