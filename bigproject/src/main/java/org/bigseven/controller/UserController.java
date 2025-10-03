package org.bigseven.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.JwtConstants;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.UserLoginRequest;
import org.bigseven.dto.user.UserRegisterRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.JwtTokenUtil;
import org.bigseven.service.UserService;
import org.bigseven.util.XssProtectionUtils;
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
    private final XssProtectionUtils xssProtectionUtils;

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录结果，包含token和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<AjaxResult<Map<String, Object>>> login(@Valid @RequestBody UserLoginRequest request) {
            Map<String, Object> result = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(AjaxResult.success(result));
    }

    /**
     * 用户注册
     *
     * @param request 注册请求参数
     * @return 注册结果，包含token和用户信息
     */
    @PostMapping("/register")
    public ResponseEntity<AjaxResult<Map<String, Object>>> register(@Valid @RequestBody UserRegisterRequest request) {
            // 先过滤和转义
            String username = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getUsername()));
            String email = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getEmail()));
            String nickname = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getNickname()));
            String password = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getPassword()));

            Map<String, Object> result = userService.register(
                    username,
                    nickname,
                    password,
                    email,
                    UserTypeEnum.STUDENT
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AjaxResult.success(result));
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
            String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
            Map<String, Object> result = userService.refreshToken(token);
            return ResponseEntity.ok(AjaxResult.success(result));
    }

}