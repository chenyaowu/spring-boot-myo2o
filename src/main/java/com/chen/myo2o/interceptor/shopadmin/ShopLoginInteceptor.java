package com.chen.myo2o.interceptor.shopadmin;

import com.chen.myo2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShopLoginInteceptor extends HandlerInterceptorAdapter {
    /**
     * 主要做事前拦截，级用户操作发生时，改写preHandle里的逻辑进行拦截
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userObject = request.getSession().getAttribute("user");
        if(userObject!=null){
            //如果用户信息不为空则将session里的用户信息装换成PersonInfo实体类对象
            PersonInfo personInfo = (PersonInfo) userObject;
            if (personInfo != null &&personInfo.getUserId() != null && personInfo.getUserId()>0 && personInfo.getEnableStatus() == 1){
                return true;
            }

        }
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("<script>");
        out.print("window.open('"+request.getContextPath()+"/local/login?usertype=2','_self')");
        out.print("</script>");
        out.print("</html>");
        return false;
    }




}
