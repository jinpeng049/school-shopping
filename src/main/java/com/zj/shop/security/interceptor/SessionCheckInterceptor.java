package com.zj.shop.security.interceptor;

import com.zj.shop.constant.GlobalConstant;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by LiZhengYong on 2016/6/20.
 */
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = Logger.getLogger(SessionCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        logger.debug(request.getRequestURL());
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/admin/index")
                || servletPath.startsWith("/admin/login")
                || servletPath.startsWith("/admin/logout")
                || servletPath.startsWith("/admin/ssoLogin")
                || servletPath.startsWith("/order/pssCancel")
                || servletPath.startsWith("/user/regPage")
                || servletPath.startsWith("/code")
                || servletPath.startsWith("/user/checkUser")
                || servletPath.startsWith("/checkCode")
                || servletPath.startsWith("/user/reg")
                || servletPath.startsWith("/json")
                || servletPath.startsWith("/nporder/pssCancel")
                || servletPath.startsWith("/pss")
                || servletPath.startsWith("/file/uploadRegPic")
                || servletPath.startsWith("/notice/noticePage")
                || servletPath.startsWith("/notice/outer")
                || servletPath.startsWith("/main/notice")
                || servletPath.startsWith("/main/test")
                || servletPath.startsWith("/api")
                || servletPath.startsWith("/app")
                || servletPath.startsWith("/test")
                || servletPath.startsWith("/order")
                ) {
            return true;
        } else {
            if (request.getSession().getAttribute(GlobalConstant.SESSION_INFO) != null) {
                return true;
            } else {
                // 未登录
                PrintWriter out = response.getWriter();
                StringBuilder builder = new StringBuilder();
                builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                builder.append("alert(\"登录过期，请重新登录\");");
                builder.append("window.top.location.href=\"");
                builder.append(contextPath);
                builder.append("/admin/index\";</script>");
                out.print(builder.toString());
                out.close();
                //response.sendRedirect(new StringBuilder().append(contextPath).append("/admin/index").toString());
                return false;
            }
        }
    }
}
