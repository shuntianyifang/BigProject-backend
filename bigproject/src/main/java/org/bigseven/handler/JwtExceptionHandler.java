package org.bigseven.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.bigseven.result.AjaxResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
@RestControllerAdvice
@Order(20)
public class JwtExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<AjaxResult<?>> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AjaxResult.fail(HttpStatus.UNAUTHORIZED.value(), "Token 已过期"));
    }
}
