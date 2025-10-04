package org.bigseven.dto.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * @author v185v
 * &#064;date 2025/9/24
 */
@Data
public class UserSimpleVO {
    @JsonProperty("user_id")
    private Integer userId;
    private String nickname;
    private String email;
    private String userPhone;
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    public UserSimpleVO() {}

}