package org.bigseven.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

@Data
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    private String username;

    private String password;

    private String email;

    private UserTypeEnum userType;
}
