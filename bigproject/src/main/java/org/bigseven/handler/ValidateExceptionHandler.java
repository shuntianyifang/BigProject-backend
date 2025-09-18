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
/**
 * 异常处理器类，用于处理各种验证和资源未找到异常
 */
public class ValidateExceptionHandler {
    /**
     * 处理参数验证相关的异常，包括方法参数验证失败、JSON映射异常、HTTP消息不可读异常和Servlet请求绑定异常
     * @param e 异常对象，可以是MethodArgumentNotValidException、JsonMappingException、
     *          HttpMessageNotReadableException或ServletRequestBindingException类型
     * @return 返回AjaxResult对象，包含参数无效的错误信息
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            JsonMappingException.class,
            HttpMessageNotReadableException.class,
            ServletRequestBindingException.class
    })
    @ResponseBody
    public AjaxResult<Object> handleValidateException(Exception e) {
        /// 记录异常日志
        HandlerUtils.logException(e);
        /// 返回参数无效的错误结果
        return AjaxResult.fail(ExceptionEnum.INVALID_PARAMETER);
    }

    /**
     * 处理资源未找到相关的异常，包括资源未找到异常和HTTP请求方法不支持异常
     * @param e 异常对象，可以是NoResourceFoundException或HttpRequestMethodNotSupportedException类型
     * @return 返回AjaxResult对象，包含资源未找到的错误信息
     */
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
