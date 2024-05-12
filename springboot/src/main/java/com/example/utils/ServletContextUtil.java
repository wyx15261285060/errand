package com.example.utils;/*
 *@author yviang wi
 *@date  2024/5/11 11:33
 *@version 1.0
 *@email：3060491854@qq.com
 */

import javax.servlet.http.HttpServletRequest;

public class ServletContextUtil {
    private static final ThreadLocal<HttpServletRequest> THREAD_LOCAL_REQUEST = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        THREAD_LOCAL_REQUEST.set(request);
    }

    public static HttpServletRequest getRequest() {
        return THREAD_LOCAL_REQUEST.get();
    }

    public static void clearRequest() {
        THREAD_LOCAL_REQUEST.remove();
    }
}
