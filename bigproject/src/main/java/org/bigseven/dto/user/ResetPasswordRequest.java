package org.bigseven.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Data
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    private String username;

    private String password;

    @JsonProperty("new_password")
    private String newPassword;
}
