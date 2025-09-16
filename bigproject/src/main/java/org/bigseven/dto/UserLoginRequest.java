package org.bigseven.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    private String username;

    private String password;

    private String email;

}
