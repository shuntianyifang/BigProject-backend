package org.bigseven.dto.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * 用户简单信息
 * 用于返回用户简单信息
 * 仅返回用户ID、昵称、邮箱、手机号、用户类型
 *
 * @author v185v
 * &#064;date 2025/9/24
 */
@Data
public class UserSimpleVO {
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String userPhone;
    /**
     * 用户类型
     */
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    public UserSimpleVO() {}

}