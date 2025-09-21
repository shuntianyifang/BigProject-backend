package org.bigseven.exception;

import lombok.Getter;
import org.bigseven.constant.ExceptionEnum;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/15
 */
@Getter
public class ApiException extends RuntimeException {
    private final Integer errorCode;
    private final String errorMsg;

    public ApiException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

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
