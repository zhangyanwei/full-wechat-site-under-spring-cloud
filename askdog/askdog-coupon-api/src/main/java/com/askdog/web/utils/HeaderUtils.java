package com.askdog.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.google.common.base.Splitter.on;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.getFirst;

public class HeaderUtils {

    public static String getRequestRealIp(HttpServletRequest request) {
        // multi-tier
        String ip = request.getHeader("X-Forwarded-For");
        if (!isNullOrEmpty(ip)) {
            ip = getFirst(on(',').trimResults().omitEmptyStrings().split(ip), null);
        }
        if (!isResolved(ip)) {
            // single-tier
            ip = request.getHeader("X-Real-IP");
        }
        if (!isResolved(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                } catch (UnknownHostException ignored) {}
            }
        }

        return ip;
    }

    public static String getLocalIp() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException ignored) {}

        return null;
    }

    private static boolean isResolved(String ip) {
        return !isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }
}
