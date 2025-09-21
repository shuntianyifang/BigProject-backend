package org.bigseven.handler;

import org.bigseven.exception.ApiException;
import org.bigseven.result.AjaxResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/16
 */
@ControllerAdvice
@Order(50)
public class CustomExceptionHandler {
    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public AjaxResult<Object> handleApiException(ApiException e) {
        return AjaxResult.fail(e.getErrorCode(), e.getErrorMsg());
    }
}
