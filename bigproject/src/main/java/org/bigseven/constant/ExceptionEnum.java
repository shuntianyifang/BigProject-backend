package org.bigseven.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务状态码类型：
 * 10XXX:系统内部错误
 * 20XXX:公共业务异常
 * 30XXX:用户相关
 * 40XXX:反馈相关
 * 50XXX:评价相关
 *
 * @author shuntianyifang
 * &#064;date 2025/9/15
 */

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    /// 系统
    SERVER_ERROR(10000,"系统错误"),
    /// 公共
    INVALID_PARAMETER(20000,"参数错误"),
    PERMISSION_DENIED(20001,"权限不足"),
    RESOURCE_NOT_FOUND(20002,"资源不存在"),
    OPERATION_FAILED(20003,"操作失败"),
    BAD_REQUEST(20400,HttpStatus.BAD_REQUEST.getReasonPhrase()),
    UNAUTHORIZED(20401,HttpStatus.UNAUTHORIZED.getReasonPhrase()),
    NOT_FOUND_ERROR(20404, HttpStatus.NOT_FOUND.getReasonPhrase()),
    /// 用户
    USER_NOT_EXIST(30000,"用户不存在"),
    USERNAME_OR_PASSWORD_WRONG(30001,"用户名或密码错误"),
    USER_DISABLED(30002,"用户已被禁用"),
    USER_EXIST(30003,"用户已存在"),
    EMAIL_EXIST(30004,"邮箱已被注册"),
    ILLEGAL_USER_TYPE(30005,"非法的用户类型"),
    /// 反馈
    FEEDBACK_NOT_FOUND(40000,"反馈不存在"),
    ILLEGAL_FEEDBACK_TYPE(40001,"未知的反馈类型"),
    FEEDBACK_CANNOT_UPDATE(40002,"该反馈无法修改"),
    FEEDBACK_ALREADY_CONFIRMED(40003,"该反馈对应的回复已确认收到"),
    /// 评价
    RATING_EXIST(50000,"您已评价过该回复"),
    RATING_NOT_FOUND(50001,"评价不存在");
    private final Integer errorCode;
    private final String errorMsg;
}
