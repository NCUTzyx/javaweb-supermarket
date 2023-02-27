package com.zyx.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author 张宇森
 * @version 1.0
 */

//编码过滤器
@SuppressWarnings("all")
public class Encode_filter implements Filter{
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        chain.doFilter(request,response);
    }

    public void destroy() {

    }
}
