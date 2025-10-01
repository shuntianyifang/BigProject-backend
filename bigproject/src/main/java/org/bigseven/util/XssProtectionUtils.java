package org.bigseven.util;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/1
 */
@Component
public class XssProtectionUtils {
    public String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // 只允许字母、数字、下划线、中文，其他字符去除
        return input.replaceAll("[<>\"'%;()&+]", "");
    }

    public String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return StringEscapeUtils.escapeHtml4(input);
    }
}
