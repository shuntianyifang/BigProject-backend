package org.bigseven.dto.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/29
 */
@Data
@Builder
public class GetUserListElement {
    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    private String email;

    @JsonProperty("user_phone")
    private String userPhone;

    @JsonProperty("user_type")
    private UserTypeEnum userType;

    private String nickname;

    @JsonProperty("real_name")
    private String realName;

    private Boolean deleted;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("last_login_at")
    private LocalDateTime lastLoginAt;

}
