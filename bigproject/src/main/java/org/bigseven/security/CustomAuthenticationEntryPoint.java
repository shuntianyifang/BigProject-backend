package org.bigseven.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNKNOWN = "unknown";
    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP_HEADER = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP_HEADER = "WL-Proxy-Client-IP";

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        /// 收集请求信息
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String authHeader = request.getHeader("Authorization");

        /// 记录未认证访问尝试
        logger.warn("未认证访问尝试 - IP: {}, 方法: {}, 路径: {}, User-Agent: {}, Authorization头: {}, 原因: {}",
                remoteAddr, method, requestUrl, userAgent, authHeader, authException.getMessage());

        /// 设置响应
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication failed\"}");
    }

    /// 获取客户端真实IP（考虑代理情况）
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR_HEADER);
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP_HEADER);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP_HEADER);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}