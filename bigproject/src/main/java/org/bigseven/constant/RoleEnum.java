package org.bigseven.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    STUDENT("学生"),
    ADMIN("管理员"),
    SUPER_ADMIN("超级管理员");

    private final String displayName;

    RoleEnum(String displayName) {
        this.displayName = displayName;
    }

}