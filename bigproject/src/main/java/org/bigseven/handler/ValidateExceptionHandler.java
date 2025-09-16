package org.bigseven.handler;

import com.fasterxml.jackson.databind.JsonMappingException;

import org.bigseven.constant.ExceptionEnum;
import org.bigseven.result.AjaxResult;
import org.bigseven.util.HandlerUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(10)
public class ValidateExceptionHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            JsonMappingException.class,
            HttpMessageNotReadableException.class,
            ServletRequestBindingException.class
    })
    @ResponseBody
    public AjaxResult<Object> handleValidateException(Exception e) {
        HandlerUtils.logException(e);
        return AjaxResult.fail(ExceptionEnum.INVALID_PARAMETER);
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            HttpRequestMethodNotSupportedException.class,
    })
    @ResponseBody
    public AjaxResult<Object> handleNotFoundException(Exception e) {
        HandlerUtils.logException(e);
        return AjaxResult.fail(ExceptionEnum.NOT_FOUND_ERROR);
    }
}
