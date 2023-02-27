package com.zyx.service.user;

import com.zyx.dao.BaseDao;
import com.zyx.dao.user.UserDao;
import com.zyx.dao.user.UserDaoImpl;
import com.zyx.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 张宇森
 * @version 1.0
 */
//业务层调用Dao层
@SuppressWarnings("all")
public class UserServiceImpl implements UserService{

    //因为业务层调用Dao层,引入Dao层
    private UserDao userDao;

    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    //用户登录
    @Override
    public User login(String userCode, String password) {

        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return user;
    }
    //修改密码
    @Override
    public boolean updatePwd(int id, String password) throws SQLException {

        Connection connection = null;
        boolean flag = false;
        connection = BaseDao.getConnection();

        //修改密码
        if(userDao.updatePwd(connection,id,password)>0){
            flag = true;
        }
        BaseDao.closeResources(connection,null,null);
        return flag;
    }

    //查询用户数
    @Override
    public int getUserCount(String username, int userRole) {

        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return count;
    }

    //查询用户列表
    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return userList;
    }

    //增加用户信息
    @Override
    public boolean add(User user) {

        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if (updateRows > 0) {
                flag = true;
                System.out.println("add success!");
            } else {
                System.out.println("add failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            //在service层进行connection连接的关闭
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    //删除用户
    @Override
    public boolean deleteUserById(Integer delId) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.deleteUserById(connection, delId) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    //根据userCode查询用户
    @Override
    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return user;
    }

    //根据ID查找用户
    @Override
    public User getUserById(String id) {
        // TODO Auto-generated method stub
        User user = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection, id);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            user = null;
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return user;
    }

    //修改用户信息
    @Override
    public boolean modify(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.modify(connection, user) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }
}
