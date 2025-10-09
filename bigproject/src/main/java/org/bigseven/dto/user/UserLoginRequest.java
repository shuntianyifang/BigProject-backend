package org.bigseven.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 用户登录请求数据传输对象
 * 用于封装用户登录请求参数
 *
 * @author v185v
 * &#064;date 2025/9/16
 */
@Data
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
