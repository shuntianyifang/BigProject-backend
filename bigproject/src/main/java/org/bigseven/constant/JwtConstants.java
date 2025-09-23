package org.bigseven.constant;

/**
 * JWT 相关常量
 *
 * @author shuntianyifang
 * &#064;date  2025/9/23
 */
public class JwtConstants {

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token 头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token 声明字段
     */
    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_AUTHORITIES = "authorities";
    public static final String CLAIM_KEY_CREATED = "created";

    /**
     * Token 类型
     */
    public static final String TOKEN_TYPE_BEARER = "Bearer";

    /**
     * 默认过期时间（秒）
     */
    public static final long DEFAULT_EXPIRATION = 3600L;

    private JwtConstants() {
        // 防止实例化
    }
}