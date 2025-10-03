package org.bigseven.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
@Configuration
public class SecurityHeaderConfig {

    @Bean
    public Filter securityHeadersFilter() {
        return (ServletRequest request, ServletResponse response, FilterChain chain) -> {
            if (response instanceof HttpServletResponse resp) {
                resp.setHeader("X-Content-Type-Options", "nosniff");
                resp.setHeader("X-Frame-Options", "DENY");
                resp.setHeader("X-XSS-Protection", "1; mode=block");
                resp.setHeader("Content-Security-Policy", "default-src 'self'");
            }
            chain.doFilter(request, response);
        };
    }
}