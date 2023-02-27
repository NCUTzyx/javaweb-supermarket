package com.zyx.servlet.user;

import com.zyx.pojo.User;
import com.zyx.service.user.UserService;
import com.zyx.service.user.UserServiceImpl;
import com.zyx.util.Constants;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 张宇森
 * @version 1.0
 */
//控制层: 调用业务层代码
public class LoginServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取用户名和密码
        String username = req.getParameter("userCode");
        String password = req.getParameter("userPassword");

        //和数据库中的密码进行对比，调用业务层
        UserService userService = new UserServiceImpl();
        User user = userService.login(username, password);
        if(user != null && password.equals(user.getUserPassword())){  //查有此人
            //将用户的信息放到Session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //跳转到内部主页
            resp.sendRedirect("jsp/frame.jsp");
        }else{   //查无此人,无法登录
            //转发回登陆界面，顺带提示用户名或者密码错误
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
