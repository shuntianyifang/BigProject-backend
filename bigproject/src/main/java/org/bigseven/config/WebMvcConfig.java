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
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射上传文件访问路径
        registry.addResourceHandler("/file/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
