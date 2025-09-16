package org.bigseven.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    @JsonProperty("user_id")
    private Integer userId;

    private String username;

    private String password;

    private String email;
    @TableField("user_type")
    @JsonProperty("user_type")
    private UserTypeEnum userType;

    private String realName;

    private String userPhone;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public UserTypeEnum getUserType(){
        return userType;
    }

    public void setUserType(UserTypeEnum userType){
        this.userType = userType;
    }
}
