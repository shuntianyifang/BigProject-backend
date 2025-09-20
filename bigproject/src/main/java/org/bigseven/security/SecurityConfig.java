package org.bigseven.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 不懂怎么配置的可以将对应的代码拿出喂AI
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          AuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
    /**
     * 配置Spring Security的安全过滤器链
     *
     * @param http HttpSecurity对象，用于配置安全策略
     * @return SecurityFilterChain 安全过滤器链配置
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) /// 启用CORS配置
                .csrf(AbstractHttpConfigurer::disable) /// 禁用CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) /// 无状态会话
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/auth/**",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**").permitAll() /// 允许未经认证访问"/url"
                        .anyRequest().authenticated() /// 其他请求需要认证
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint) /// 使用自定义认证入口点
                        .accessDeniedHandler(customAccessDeniedHandler) /// 使用自定义的访问拒绝处理器
                )
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class); /// 添加JWT过滤器

        return http.build();
    }

    /// 认证失败处理
    /**<p>
     * 创建并配置认证入口点Bean
     * <p>
     * 该方法定义了当未认证用户尝试访问受保护资源时的处理逻辑。
     * 当认证失败时，会返回401状态码和JSON格式的错误信息。
     * </p>
     *<p>
     * @return AuthenticationEntryPoint 认证入口点实例
     */

    //用了自定义的authenticationEntryPoint，原来的直接删了

    /// 授权失败处理
    /**
     * 创建并配置访问拒绝处理器Bean
     *
     * @return AccessDeniedHandler 访问拒绝处理器实例
     *
     * 该处理器用于处理用户访问被拒绝的情况，返回403状态码和JSON格式的错误信息
     */

    //用了自定义的AccessDeniedHandler，原来的直接删了

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**<p>
     * 配置跨域资源共享(CORS)策略
     *<p>
     * 该方法创建并配置CORS配置源，允许前端应用跨域访问后端API资源。
     * 配置包括允许的来源、HTTP方法、请求头和暴露的响应头等信息。
     *<p>
     * @return CorsConfigurationSource CORS配置源对象，用于处理跨域请求
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}