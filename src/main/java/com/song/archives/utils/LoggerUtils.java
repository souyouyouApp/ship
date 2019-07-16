package com.song.archives.utils;

import javax.servlet.http.HttpServletRequest;

public final class LoggerUtils {

    public static final String LOGGER_RETURN = "_logger_return";
    public static final String LOGGER_RESULT = "result";
    public static final String LOGGER_DESC = "description";


    private LoggerUtils() {
    }

    public static void setLoggerSuccess(HttpServletRequest request){
        request.setAttribute(LOGGER_RESULT,"成功");
    }

    public static void setLoggerFailed(HttpServletRequest request){
        request.setAttribute(LOGGER_RESULT,"失败");
    }

    public static void setLoggerFailed(HttpServletRequest request,String desc){
        request.setAttribute(LOGGER_RESULT,desc);
    }

    public static void setLoggerReturn(HttpServletRequest request,String msg){
        if (msg.equals("success")){
            setLoggerSuccess(request);
        }else {
            setLoggerFailed(request,msg);
        }
    }

    /**
     * 获取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getCliectIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static String getRequestType(HttpServletRequest request) {
        return request.getHeader("X-Requested-With");
    }
}