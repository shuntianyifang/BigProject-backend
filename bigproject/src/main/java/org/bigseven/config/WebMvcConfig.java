package org.bigseven.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author v185v
 * &#064;date 2025/9/18
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加静态资源映射，将 /uploads/** 映射到文件存储目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/opt/BigProject/uploads/");
    }
}
