package org.bigseven.dto.superadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.UserTypeEnum;

/**
 * @author v185v
 * &#064;date 2025/9/25
 */
@Data
@AllArgsConstructor
@Builder
public class ChangeUserTypeRequest {
    @JsonProperty("user_type")
    private UserTypeEnum userType;
}
