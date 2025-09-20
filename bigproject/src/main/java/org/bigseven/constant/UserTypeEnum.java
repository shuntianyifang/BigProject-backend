package org.bigseven.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 * 定义系统中不同类型的用户及其权限级别
 * @author v185v
 */

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    /**
     * 学生用户，具有基本的查看和有限的操作权限
     */
    STUDENT(0,"学生"),
    /**
     * 管理员用户，具有系统管理功能，但不包括最高权限操作
     */
    ADMIN(1,"管理员"),
    /**
     * 超级管理员，具有系统的所有权限，包括用户管理和系统配置
     */
    SUPER_ADMIN(2,"超级管理员");

    /**
     * 用户类型编码，用于数据库存储和程序逻辑判断
     */
    private final Integer userType;
    /**
     * 用户类型显示名称，用于界面展示
     */
    private final String displayName;
}