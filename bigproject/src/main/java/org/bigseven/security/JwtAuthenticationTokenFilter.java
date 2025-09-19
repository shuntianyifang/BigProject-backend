package org.bigseven.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /// 提取令牌，验证令牌，然后设置认证信息
    /**
     * 执行JWT认证过滤器的内部逻辑
     * 该方法用于拦截HTTP请求，验证JWT token的有效性，并设置Spring Security的认证信息
     *
     * @param httpServletRequest  HTTP请求对象，包含客户端发送的请求信息
     * @param httpServletResponse HTTP响应对象，用于向客户端发送响应信息
     * @param filterChain         过滤器链，用于继续执行后续的过滤器
     * @throws ServletException 当Servlet处理出现异常时抛出
     * @throws IOException      当IO操作出现异常时抛出
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        ///从请求头中获取认证信息
        String authHeader = httpServletRequest
                .getHeader(jwtTokenUtil.getHeader());
        if (StringUtils.isNotEmpty(authHeader)) {
            try {
                ///从token中解析用户名
                String username = jwtTokenUtil.getUsernameFromToken(authHeader);
                ///如果用户名存在且当前上下文中没有认证信息，则进行认证
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    ///验证token是否有效
                    if (jwtTokenUtil.validateToken(authHeader, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                /// 记录日志，并返回401错误
                logger.error("JWT token验证失败: {}", e.getMessage(), e);
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write("Invalid or expired token");
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
