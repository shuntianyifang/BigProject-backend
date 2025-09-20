package org.bigseven.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuntianyifang
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "feedback")
public class FeedbackConfig {
    private int maxImages = 9;

}