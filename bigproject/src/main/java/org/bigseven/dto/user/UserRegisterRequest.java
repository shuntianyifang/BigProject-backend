package org.bigseven.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * 用户注册请求数据传输对象
 * 用于封装用户注册时的请求参数
 *
 * @author v185v
 * &#064;date 2025/9/17
 */
@Data
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    /**
     * 用户名
     */
    @Size(min=1, max=20,message = "用户名长度必须在1-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$", message = "用户名格式不正确")
    private String username;

    /**
     * 昵称
     */
    @Size(min=1, max=20,message = "用户名长度必须在1-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$", message = "用户名格式不正确")
    private String nickname;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
}
