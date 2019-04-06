package com.chen.myo2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    public static int getInt(HttpServletRequest request, String key){
        try {
            return Integer.parseInt(request.getParameter(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public static Long getLong(HttpServletRequest request, String key){
        try {
            String s = request.getParameter(key);
            return Long.parseLong(request.getParameter(key));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static Double getDouble(HttpServletRequest request, String key){
        try {
            return Double.parseDouble(request.getParameter(key));
        } catch (NumberFormatException e) {
            return -1d;
        }
    }

    public static Boolean getBoolean(HttpServletRequest request, String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String key){
        try {
            String result = request.getParameter(key);
            if(result != null){
                result = result.trim();
            }
            if("".equals(result)){
                result = null;
            }
            return result;
        } catch (Exception e) {
           return null;
        }

    }
}
