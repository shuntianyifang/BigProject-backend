package org.bigseven.dto.user;
import lombok.Data;

@Data
public class UserSimpleVO {
    private Integer id;
    private String username;
    private String realName;
    private String email;
}