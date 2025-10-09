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
 * 用户实体类
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
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    @TableField("user_phone")
    @JsonProperty("user_phone")
    private String userPhone;

    /**
     * 用户类型
     */
    @TableField("user_type")
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    @JsonProperty("real_name")
    private String realName;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastLoginAt;

}
