package org.bigseven.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.util.HttpLogColorUtils;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * 访问日志过滤器，记录HTTP请求的处理时间、状态码、请求方法等信息并输出彩色日志
 *
 * @author shuntianyifang
 * &#064;date  2025/9/16
 */
@Slf4j
@Component
public class AccessLogFilter implements Filter {

    private static final int METHOD_NAME_MAX_LENGTH = 6;
    private static final String UNKNOWN_IP = "unknown";

    static {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest httpRequest) || !(response instanceof HttpServletResponse httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        Instant startTime = Instant.now();
        try {
            chain.doFilter(request, response);
        } finally {
            Instant endTime = Instant.now();
            logRequestDetails(httpRequest, httpResponse, startTime, endTime);
        }
    }

    /**
     * 记录请求详细信息到日志
     */
    private void logRequestDetails(HttpServletRequest request, HttpServletResponse response,
                                   Instant startTime, Instant endTime) {
        long durationMs = Duration.between(startTime, endTime).toMillis();
        int statusCode = response.getStatus();
        String method = request.getMethod();
        String requestUri = getRequestUriWithQuery(request);
        String clientIp = getClientIpAddress(request);

        String coloredStatusCode = HttpLogColorUtils.colorizeStatus(statusCode);
        String coloredMethod = HttpLogColorUtils.colorizeMethod(method);
        String coloredDuration = HttpLogColorUtils.colorizeDuration(durationMs);
        String coloredIp = HttpLogColorUtils.colorizeIp(clientIp);

        log.info("{} | {}ms | {} {} | IP: {}",
                coloredStatusCode,
                coloredDuration,
                formatMethod(coloredMethod),
                requestUri,
                coloredIp);
    }

    /**
     * 获取带查询参数的请求URI
     */
    private String getRequestUriWithQuery(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String requestUri = request.getRequestURI();
        return queryString != null ? requestUri + "?" + queryString : requestUri;
    }

    /**
     * 获取客户端IP地址，支持X-Forwarded-For头
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }

    /**
     * 格式化方法名称，确保固定宽度
     */
    private String formatMethod(String method) {
        // 去除ANSI颜色代码来计算实际长度
        String plainMethod = method.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = METHOD_NAME_MAX_LENGTH - plainMethod.length();
        return padding > 0 ? method + " ".repeat(padding) : method;
    }
}