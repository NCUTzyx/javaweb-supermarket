package com.zyx.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyx.pojo.Role;
import com.zyx.pojo.User;
import com.zyx.service.role.RoleService;
import com.zyx.service.role.RoleServiceImpl;
import com.zyx.service.user.UserService;
import com.zyx.service.user.UserServiceImpl;
import com.zyx.util.Constants;
import com.zyx.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");

        if (method != null && method.equals("add")) {
            //增加操作
            this.add(request, response);
        } else if (method != null && method.equals("query")) {
            this.query(request, response);
        } else if (method != null && method.equals("getrolelist")) {
            this.getRoleList(request, response);
        } else if (method != null && method.equals("ucexist")) {
            this.userCodeExist(request, response);
        } else if (method != null && method.equals("deluser")) {
            this.delUser(request, response);
        } else if (method != null && method.equals("view")) {
            this.getUserById(request, response, "userview.jsp");
        } else if (method != null && method.equals("modify")) {
            this.getUserById(request, response, "usermodify.jsp");
        } else if (method != null && method.equals("modifyexe")) {
            this.modify(request, response);
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(request, response);
        } else if (method != null && method.equals("savepwd")) {
            this.updatePwd(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //从Session里拿id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;

        if(o!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            try {
                flag= userService.updatePwd(((User) o).getId(), newpassword);
                if(flag){
                    //密码修改成功
                    req.setAttribute("message","修改密码成功");
                    //移除当前session
                    req.getSession().removeAttribute(Constants.USER_SESSION);
                }else{
                    //密码修改失败
                    req.setAttribute("message","修改密码失败");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            //密码修改失败
            req.setAttribute("message","新密码修改有误");
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);

    }

    //验证旧密码 session中有用户的密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        //从Session里拿id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map => 结果集
        Map<String, String> resultMap = new HashMap();

        if(o==null){
            //session失效了
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){  //输入的密码为空
            resultMap.put("result","error");
        }else {
            //Session中用户的密码
            String userPassword = ((User) o).getUserPassword();
            if(oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else{
                resultMap.put("result","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询用户列表
    public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //从前端获取数据
        String queryUserName = request.getParameter("queryname");
        String temp = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        int queryUserRole = 0;
        UserService userService = new UserServiceImpl();
        List<User> userList = null;
        //首次请求，第一页，且页面大小固定
        //设置页面容量
        int pageSize = Constants.pageSize;
        //默认当前页码
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }

        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                response.sendRedirect("error.jsp");
            }
        }
        //总数量（表）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);  //当前页
        pages.setPageSize(pageSize);            //页面大小
        pages.setTotalCount(totalCount);        //总数量

        int totalPageCount = pages.getTotalPageCount();  //总页面数量

        //控制首页和尾页
        //如果页面小于第一页，就显示第一页
        if (currentPageNo < 1) {
            currentPageNo = 1;
            //如果当前页面大于最后一页，当前页等于最后一页即可
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        request.setAttribute("userList", userList);
        //获取角色列表
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        request.setAttribute("roleList", roleList);

        request.setAttribute("queryUserName", queryUserName);
        request.setAttribute("queryUserRole", queryUserRole);
        request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);

        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    //修改用户信息
    public void modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if (userService.modify(user)) {
            response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
        } else {
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }

    }

    //根据ID查找用户
    public void getUserById(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        String id = request.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    //删除用户
    public void delUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try {
            delId = Integer.parseInt(id);
        } catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (delId <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            UserService userService = new UserServiceImpl();
            if (userService.deleteUserById(delId)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //根据userCode查询用户
    public void userCodeExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断用户账号是否可用
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }

        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        response.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = response.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    //添加用户
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if (userService.add(user)) {
            response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
        } else {
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }

    }

    //角色列表
    public void getRoleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

}

