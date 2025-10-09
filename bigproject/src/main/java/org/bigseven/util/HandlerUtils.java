package org.bigseven.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 错误处理工具类
 *
 * @author shuntianyifang
 * &#064;date 2025/9/15
 */
@Slf4j
public class HandlerUtils {
    public static void logException(Exception e){
        log.error("Caught an Exception",e);
    }
}
