package com.zyx.filter;

import com.zyx.pojo.User;
import com.zyx.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class Sys_filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);

        if(user==null){   //被移除，或者未登录
            resp.sendRedirect("/store/error.jsp");
        }else{
            chain.doFilter(request,response);
        }


    }

    @Override
    public void destroy() {

    }
}
