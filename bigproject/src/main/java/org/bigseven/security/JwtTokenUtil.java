package org.bigseven.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.builder;

@Component
public class JwtTokenUtil implements Serializable {
    @Getter
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Getter
    @Value("${jwt.header:Authorization}")
    private String header;

    private final SecretKey key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretString) {
        /// 将字符串密钥转换为安全的SecretKey
        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT令牌
     *
     * @param claims 令牌中包含的声明信息
     * @return 生成的JWT令牌字符串
     */
    private String generateToken(Map<String, Object> claims) {
        /// 计算令牌过期时间
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);

        return builder()
                .claims(claims)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /// 从令牌中获取声明
    /**
     * 从JWT token中解析出Claims对象
     *
     * @param token JWT令牌字符串
     * @return 解析成功的Claims对象，如果解析失败则返回null
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /// 从UserDetails生成令牌
    /**
     * 根据用户详情生成JWT令牌
     *
     * @param userDetails 用户详情对象，包含用户名和权限信息
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("sub", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /// 从令牌获取username
    /**
     * 从JWT token中提取用户名
     *
     * @param token JWT token字符串
     * @return 返回从token中解析出的用户名，如果解析失败则返回null
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();

        } catch (Exception e) {
            username = null;

        }
        return username;

    }

    /// 检查令牌是否过期
    /**
     * 判断JWT token是否已过期
     *
     * @param token JWT token字符串
     * @return 如果token已过期返回true，否则返回false；如果解析token时发生异常也返回false
     */
    public Boolean isTokenExpired(String token) {
        try {
            /// 解析token获取声明信息
            Claims claims = getClaimsFromToken(token);
            /// 获取token的过期时间
            Date expiration = claims.getExpiration();
            /// 判断过期时间是否在当前时间之前
            return expiration.before(new Date());
        } catch (Exception e) {
            /// 发生异常时默认认为token未过期
            return false;
        }
    }

    /// 刷新令牌
    /**
     * 刷新JWT令牌
     *
     * @param token 原始JWT令牌字符串
     * @return 刷新后的JWT令牌字符串，如果刷新失败则返回null
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            /// 解析原始令牌获取声明信息，并更新创建时间后生成新的令牌
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);

        } catch (Exception e) {
            refreshedToken = null;

        }
        return refreshedToken;
    }

    /// 返回user
    /**
     * 验证JWT令牌的有效性
     *
     * @param token 待验证的JWT令牌字符串
     * @param userDetails 包含用户详细信息的对象，用于验证令牌中的用户名
     * @return Boolean 令牌有效返回true，无效返回false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username != null &&
                username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }

}