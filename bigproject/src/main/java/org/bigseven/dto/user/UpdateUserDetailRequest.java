package org.bigseven.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@Data
@AllArgsConstructor
@Builder
public class UpdateUserDetailRequest {
    @Email(message = "邮箱格式错误")
    private String email;

    @JsonProperty("user_phone")
    private String userPhone;

    @Size(max=20,message = "用户名长度必须在1-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$", message = "用户名格式不正确")
    private String nickname;

    @JsonProperty("real_name")
    @Size(max=20,message = "用户名长度必须在1-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$", message = "用户名格式不正确")
    private String realName;
}
