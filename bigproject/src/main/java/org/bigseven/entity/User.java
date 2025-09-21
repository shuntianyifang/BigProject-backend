package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.constant.UserTypeEnum;

import java.time.LocalDateTime;

/**
 * 
 * &#064;TableName  user
 * @author v185v
 * &#064;date 2025/9/16
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @TableId(type = IdType.AUTO)
    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    private String password;

    private String email;

    @TableField("user_phone")
    @JsonProperty("user_phone")
    private String userPhone;

    @TableField("user_type")
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    private String nickname;

    @TableField("real_name")
    @JsonProperty("real_name")
    private String realName;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
