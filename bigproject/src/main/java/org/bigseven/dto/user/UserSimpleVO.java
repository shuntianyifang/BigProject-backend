package org.bigseven.dto.user;
import lombok.Data;

/**
 * @author v185v
 * &#064;date 2025/9/24
 */
@Data
public class UserSimpleVO {
    private Integer id;
    private String username;
    private String realName;
    private String email;
}