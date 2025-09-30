package org.bigseven.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.JwtConstants;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.UserLoginRequest;
import org.bigseven.dto.user.UserRegisterRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.JwtTokenUtil;
import org.bigseven.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author v185v
 * &#064;date 2025/9/16
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录结果，包含token和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<AjaxResult<Map<String, Object>>> login(@Valid @RequestBody UserLoginRequest request) {
        try {
            Map<String, Object> result = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(AjaxResult.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AjaxResult.fail(ExceptionEnum.UNAUTHORIZED.getErrorCode(), e.getMessage()));
        }
    }

    /**
     * 用户注册
     *
     * @param request 注册请求参数
     * @return 注册结果，包含token和用户信息
     */
    @PostMapping("/register")
    public ResponseEntity<AjaxResult<Map<String, Object>>> register(@Valid @RequestBody UserRegisterRequest request) {
        try {
            Map<String, Object> result = userService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail(),
                    UserTypeEnum.STUDENT
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AjaxResult.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AjaxResult.fail(ExceptionEnum.BAD_REQUEST.getErrorCode(), e.getMessage()));
        }
    }

    /**
     * 刷新token
     *
     * @param authorizationHeader 包含Bearer token的Authorization头
     * @return 新的token信息
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AjaxResult<Map<String, Object>>> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AjaxResult.fail(ExceptionEnum.BAD_REQUEST.getErrorCode(), "缺少有效的token"));
        }

        try {
            String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
            Map<String, Object> result = userService.refreshToken(token);
            return ResponseEntity.ok(AjaxResult.success(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AjaxResult.fail(ExceptionEnum.UNAUTHORIZED.getErrorCode(), e.getMessage()));
        }
    }

}