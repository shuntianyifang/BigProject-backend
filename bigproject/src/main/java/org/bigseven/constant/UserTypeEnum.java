package org.bigseven.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {
    STUDENT(0,"学生"),
    ADMIN(1,"管理员"),
    SUPER_ADMIN(2,"超级管理员");
    private final Integer userType;
    private final String displayName;
}