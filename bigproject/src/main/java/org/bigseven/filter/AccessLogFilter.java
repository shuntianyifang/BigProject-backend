package org.bigseven.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class AccessLogFilter implements Filter {

    static {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
    }

    /// HTTP状态码颜色
    private static AnsiColor getStatusColor(int status){
        return switch (status / 100)  {
            case 2 -> AnsiColor.GREEN;
            case 4 -> AnsiColor.YELLOW;
            case 5 -> AnsiColor.RED;
            default -> AnsiColor.DEFAULT;
        };
    }

    /// HTTP方法颜色
    private static AnsiColor getMethodColor(String method){
        return switch (method){
            case "GET" -> AnsiColor.GREEN;
            case "POST" -> AnsiColor.YELLOW;
            case "PUT" -> AnsiColor.BLUE;
            case "DELETE" -> AnsiColor.RED;
            default -> AnsiColor.DEFAULT;
        };
    }

    private static String getRemoteAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        return ip == null ? request.getRemoteAddr() : ip;
    }

    /**
     * 执行过滤器逻辑，记录请求的处理时间、状态码、请求方法等信息并输出到日志
     *
     * @param request  Servlet请求对象，用于获取请求相关信息
     * @param response Servlet响应对象，用于获取响应状态码等信息
     * @param chain    过滤器链，用于继续执行后续过滤器或目标资源
     * @throws IOException      IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 检查请求和响应对象是否为HTTP类型，如果不是则直接放行
        if (!(request instanceof HttpServletRequest req) || !(response instanceof HttpServletResponse res)) {
            chain.doFilter(request, response);
            return;
        }

        // 记录请求开始时间
        Instant start = Instant.now();
        chain.doFilter(request, response);
        // 记录请求结束时间
        Instant end = Instant.now();
        // 计算请求处理耗时(毫秒)
        long durationMs = Duration.between(start, end).toMillis();

        // 获取响应状态码
        int status = res.getStatus();

        String method = req.getMethod();
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String ip = getRemoteAddr(req);

        /// 彩色状态码
        String colorStatus = AnsiOutput.toString(
                getStatusColor(status),
                status,
                AnsiColor.DEFAULT
        );

        /// 彩色方法
        String colorMethod = AnsiOutput.toString(
                getMethodColor(method),
                method,
                AnsiColor.DEFAULT
        );

        log.info("{} | {}ms | {} {} | IP: {}",
                colorStatus,
                durationMs,
                String.format("%-6s", colorMethod),
                uri + (query != null ? "?" + query : ""),
                ip
        );
    }
}
