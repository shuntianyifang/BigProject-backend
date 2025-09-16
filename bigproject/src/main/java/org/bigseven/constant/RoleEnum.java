package org.bigseven.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    STUDENT("學生"),
    ADMIN("管理員"),
    SUPER_ADMIN("超級管理員");

    private final String displayName;

    RoleEnum(String displayName) {
        this.displayName = displayName;
    }

}