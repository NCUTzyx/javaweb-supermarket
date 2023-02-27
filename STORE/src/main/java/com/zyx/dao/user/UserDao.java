package com.zyx.dao.user;

import com.zyx.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface UserDao {

    //得到要登陆的用户
    public User getLoginUser(Connection connection,String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwd(Connection connection,int id,String password) throws SQLException;

    //查询用户总数
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException;

    //获取用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws Exception;

    //增加用户信息
    public int add(Connection connection, User user) throws Exception;

    //通过userId获取用户
    public User getUserById(Connection connection, String id) throws Exception;

    //删除用户
    public int deleteUserById(Connection connection, Integer delId) throws Exception;

    //修改用户信息
    public int modify(Connection connection, User user) throws Exception;

}
