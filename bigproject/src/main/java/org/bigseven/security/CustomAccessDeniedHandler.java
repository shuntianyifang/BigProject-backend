package org.bigseven.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        /// 记录访问被拒绝的详细信息
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        /// 获取用户信息（如果有）
        String username = "匿名用户";
        if (request.getUserPrincipal() != null) {
            username = request.getUserPrincipal().getName();
        }

        /// 记录警告日志
        logger.warn("访问被拒绝 - 用户: {}, IP: {}, 方法: {}, 路径: {}, User-Agent: {}, 原因: {}",
                username, remoteAddr, method, requestUrl, userAgent, accessDeniedException.getMessage());

        /// 设置响应
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access denied\"}");
    }
}