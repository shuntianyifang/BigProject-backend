package org.bigseven.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuntianyifang
 */
@Slf4j
public class HandlerUtils {
    public static void logException(Exception e){
        log.error("Caught an Exception",e);
    }
}
