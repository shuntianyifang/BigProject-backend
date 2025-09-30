package org.bigseven.dto.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author v185v
 * &#064;date 2025/9/24
 */
@Data
public class UserSimpleVO {
    @JsonProperty("user_id")
    private Integer userId;
    private String username;
    private String realName;
    private String email;

    public UserSimpleVO() {}

}