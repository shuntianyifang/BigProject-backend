package org.bigseven.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuntianyifang
 * &#064;date 2025/9/20
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "feedback")
public class FeedbackConfig {
    private int maxImages;
    private String imageBaseUrl;
}