package org.bigseven.handler;

import org.bigseven.constant.ExceptionEnum;
import org.bigseven.result.AjaxResult;
import org.bigseven.util.HandlerUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author shuntianyifang
 * &#064;date 2025/9/16
 */
@ControllerAdvice
@Order(1000)
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult<Object> handleGlobalException(Exception e) {
        HandlerUtils.logException(e);
        return AjaxResult.fail(ExceptionEnum.SERVER_ERROR);
    }
}
