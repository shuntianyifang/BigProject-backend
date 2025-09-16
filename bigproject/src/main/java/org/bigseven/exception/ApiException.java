package org.bigseven.exception;

import lombok.Getter;
import org.bigseven.constant.ExceptionEnum;

/// 虽然这里面有东西长的很像但别删，它们都有用

@Getter
public class ApiException extends RuntimeException {
    private final Integer errorCode;
    private final String errorMsg;

    public ApiException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /// 这玩意相比上一个能保留原始异常信息
    public ApiException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ApiException(ExceptionEnum exceptionEnum) {
        this(exceptionEnum.getErrorCode(),exceptionEnum.getErrorMsg());
    }

    public ApiException(ExceptionEnum exceptionEnum, Throwable cause) {
        this(exceptionEnum.getErrorCode(),exceptionEnum.getErrorMsg(),cause);
    }
}
