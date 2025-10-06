package org.bigseven.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.result.AjaxResult;
import org.bigseven.util.HandlerUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * JWT相关异常处理
 *
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
@RestControllerAdvice
@Order(20)
public class JwtExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<AjaxResult<?>> handleExpiredJwt(ExpiredJwtException e) {
        HandlerUtils.logException(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AjaxResult.fail(HttpStatus.UNAUTHORIZED.value(), "Token 已过期"));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public AjaxResult<Object> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        HandlerUtils.logException(e);
        return AjaxResult.fail(ExceptionEnum.PERMISSION_DENIED);
    }
}
