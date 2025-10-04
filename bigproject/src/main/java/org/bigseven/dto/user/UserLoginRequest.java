package org.bigseven.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author v185v
 * &#064;date 2025/9/16
 */
@Data
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    private String username;

    private String password;

}
