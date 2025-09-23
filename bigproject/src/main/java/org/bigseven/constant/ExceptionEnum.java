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
    BAD_REQUEST(20400,HttpStatus.BAD_REQUEST.getReasonPhrase()),
    UNAUTHORIZED(20401,HttpStatus.UNAUTHORIZED.getReasonPhrase()),
    NOT_FOUND_ERROR(20404, HttpStatus.NOT_FOUND.getReasonPhrase()),
    /// 用户
    USER_NOT_EXIST(30000,"用户不存在"),
    /// 反馈
    FEEDBACK_NOT_EXIST(40000,"反馈不存在");
    private final Integer errorCode;
    private final String errorMsg;
}
