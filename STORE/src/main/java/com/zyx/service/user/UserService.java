package com.zyx.service.user;

import com.zyx.pojo.User;

import java.sql.SQLException;
import java.util.List;


/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface UserService {
    //用户登录
    public User login(String userCode,String password);

    //修改密码
    public boolean updatePwd(int id, String password) throws SQLException;

    //查询用户总数
    public int getUserCount(String username,int userRole);

    //查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    //增加用户信息
    public boolean add(User user);

    //删除用户
    public boolean deleteUserById(Integer delId);

    //根据userCode查询用户
    public User selectUserCodeExist(String userCode);

    //根据ID查找用户
    public User getUserById(String id);

    //修改用户信息
    public boolean modify(User user);

}
