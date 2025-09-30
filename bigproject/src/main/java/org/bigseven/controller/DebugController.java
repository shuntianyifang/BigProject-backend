package org.bigseven.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import org.bigseven.constant.JwtConstants;
import org.bigseven.entity.User;
import org.bigseven.mapper.UserMapper;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/28
 */
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    public DebugController(JwtTokenUtil jwtTokenUtil, UserMapper userMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @PostMapping("/token")
    public ResponseEntity<?> debugToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
                return ResponseEntity.badRequest().body("Invalid Authorization header");
            }

            String token = authHeader.substring(7);
            Map<String, Object> result = new HashMap<>();

            // Token基本信息
            result.put("token", token);

            // 解析Claims（测试前要将JwtTokenUil里的getClaimsFromToken改为public）
            //Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            //if (claims != null) {
            //    result.put("subject", claims.getSubject());
            //    result.put("userId", claims.get(JwtConstants.CLAIM_KEY_USER_ID));
            //    result.put("authorities", claims.get(JwtConstants.CLAIM_KEY_AUTHORITIES));
            //    result.put("created", claims.get(JwtConstants.CLAIM_KEY_CREATED));
            //    result.put("expiration", claims.getExpiration());
            //} else {
            //    result.put("error", "无法解析Claims");
            //}

            // 验证Token
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null) {
                User user = userMapper.selectByUsername(username);
                if (user != null) {
                    UserDetails userDetails = new CustomUserDetails(
                            user.getUserId(), user.getUsername(), user.getPassword(),
                            // 简化，实际应该加载权限
                            Collections.emptyList()
                    );
                    boolean isValid = jwtTokenUtil.validateToken(token, userDetails);
                    result.put("tokenValid", isValid);
                    result.put("userFromDB", user);
                } else {
                    result.put("error", "用户不存在");
                }
            }

            // SecurityContext信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            result.put("securityContext", getSecurityContextInfo(authentication));

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private Map<String, Object> getSecurityContextInfo(Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        if (authentication != null) {
            info.put("authenticated", authentication.isAuthenticated());
            info.put("principalType", authentication.getPrincipal().getClass().getName());
            info.put("principal", authentication.getPrincipal().toString());
            info.put("authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        } else {
            info.put("authenticated", false);
            info.put("principalType", "null");
        }
        return info;
    }
}