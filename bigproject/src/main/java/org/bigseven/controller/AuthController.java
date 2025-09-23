package org.bigseven.controller;

import lombok.RequiredArgsConstructor;
import org.bigseven.constant.JwtConstants;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.dto.user.UserLoginRequest;
import org.bigseven.dto.user.UserRegisterRequest;
import org.bigseven.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author shuntianyifang
 * &#064;date  2025/9/22
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginRequest request) {
        try {
            Map<String, Object> result = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterRequest request) {
        try {
            Map<String, Object> result = userService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail(),
                    UserTypeEnum.STUDENT
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // 去掉"Bearer "前缀
            if (token.startsWith(JwtConstants.TOKEN_PREFIX)) {
                token = token.substring(7);
            }
            Map<String, Object> result = userService.refreshToken(token);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}