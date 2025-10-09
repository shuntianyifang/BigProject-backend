package org.bigseven.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 重置密码请求数据传输对象
 * 用于封装用户对密码进行重置的请求参数
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Data
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 新密码
     */
    @JsonProperty("new_password")
    private String newPassword;
}
