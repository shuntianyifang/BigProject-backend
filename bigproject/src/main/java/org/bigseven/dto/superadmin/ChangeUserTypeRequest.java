package org.bigseven.dto.superadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * 修改用户类型请求数据传输对象
 * 用于封装用户对用户进行修改用户类型的请求参数
 *
 * @author v185v
 * &#064;date 2025/9/25
 */
@Data
@AllArgsConstructor
@Builder
public class ChangeUserTypeRequest {
    /**
     * 用户类型枚举
     */
    @JsonProperty("user_type")
    private UserTypeEnum userType;
}
