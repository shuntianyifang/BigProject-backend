package org.bigseven.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * @author v185v
 * &#064;date 2025/9/17
 */
@Data
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @Size(min=1, max=20,message = "用户名长度必须在1-20之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Email(message = "邮箱格式错误")
    private String email;

    private UserTypeEnum userType;
}
