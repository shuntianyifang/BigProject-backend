package org.bigseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 禁用缓存以便测试
 *
 * @author v185v
 * &#064;date 2025/9/18
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保资源处理器正确配置
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/opt/BigProject/uploads/")
                // 禁用缓存以便测试
                .setCachePeriod(0);

        log.info("配置静态资源映射: /uploads/** -> file:/opt/BigProject/uploads/");
    }
}
