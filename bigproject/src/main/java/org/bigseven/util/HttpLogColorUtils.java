package org.bigseven.util;

/**
 * HTTP日志颜色工具类
 *
 * @author shuntianyifang
 * &#064;date  2025/9/22
 */
public class HttpLogColorUtils {

    /**
     * ANSI颜色代码
     */
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    /**
     * 持续时间阈值常量（单位：毫秒）
     */
    private static final long FAST_THRESHOLD = 100L;
    private static final long MEDIUM_THRESHOLD = 500L;

    /**
     * 为HTTP方法添加颜色
     */
    public static String colorizeMethod(String method) {
        if (method == null) {
            return null;
        }

        String color = switch (method.toUpperCase()) {
            case "GET" -> GREEN + BOLD;
            case "POST" -> YELLOW + BOLD;
            case "PUT", "OPTIONS", "HEAD" -> BLUE + BOLD;
            case "DELETE" -> RED + BOLD;
            case  "PATCH" -> PURPLE + BOLD;
                default -> CYAN;
        };
        return color + method + RESET;
    }

    /**
     * 为状态码添加颜色
     */
    public static String colorizeStatus(int status) {
        String color = switch (status / 100) {
            case 2 -> GREEN + BOLD;
            case 3 -> BLUE + BOLD;
            case 4 -> YELLOW;
            case 5 -> RED + BOLD;
            default -> CYAN;
        };
        return color + status + RESET;
    }

    /**
     * 为持续时间添加颜色
     */
    public static String colorizeDuration(long durationMs) {
        String color;
        if (durationMs < FAST_THRESHOLD) {
            color = GREEN;
        } else if (durationMs < MEDIUM_THRESHOLD) {
            color = YELLOW;
        } else {
            color = RED;
        }
        return color + durationMs + "ms" + RESET;
    }

    /**
     * 为IP地址添加颜色
     */
    public static String colorizeIp(String ip) {
        return CYAN + ip + RESET;
    }

}