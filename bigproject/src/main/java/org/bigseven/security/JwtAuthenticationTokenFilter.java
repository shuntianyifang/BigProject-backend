package org.bigseven.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.bigseven.constant.JwtConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author shuntianyifang
 * &#064;date  2025/9/18
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader(jwtTokenUtil.getHeader());
        logger.info("JWT Filter: 收到认证头: {}", authHeader);

        if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            try {
                String token = authHeader.substring(7);
                logger.info("JWT Filter: 提取的token: {}", token);

                String username = jwtTokenUtil.getUsernameFromToken(token);
                logger.info("JWT Filter: 从token解析的用户名: {}", username);

                if (username != null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    logger.info("JWT Filter: 加载的用户详情类型: {}", userDetails.getClass().getName());

                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        logger.info("JWT Filter: 成功设置认证信息，用户: {}", username);
                        logger.info("JWT Filter: Principal类型: {}", authentication.getPrincipal().getClass().getName());

                        if (authentication.getPrincipal() instanceof CustomUserDetails customDetails) {
                            logger.info("JWT Filter: 用户ID: {}", customDetails.getUserId());
                        }
                    } else {
                        logger.warn("JWT Filter: Token验证失败: {}", username);
                    }
                } else {
                    logger.warn("JWT Filter: 无法从token解析用户名");
                }
            } catch (Exception e) {
                logger.error("JWT Filter: Token处理异常: {}", e.getMessage(), e);
                // 清除可能存在的认证信息
                SecurityContextHolder.clearContext();
            }
        } else {
            logger.info("JWT Filter: 无有效的认证头");
            // 清除可能存在的认证信息
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
