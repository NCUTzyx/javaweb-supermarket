package com.zyx.dao.user;

import com.mysql.jdbc.StringUtils;
import com.zyx.dao.BaseDao;
import com.zyx.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张宇森
 * @version 1.0
 */
//Dao层调用数据库
@SuppressWarnings("all")
public class UserDaoImpl implements UserDao {

    //得到要登陆的用户
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        if(connection!=null){
            String sql = "SELECT * FROM store_user where userCode=?";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, sql, params, resultSet , preparedStatement);

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getTimestamp("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            BaseDao.closeResources(null,preparedStatement,resultSet);
        }
        return user;
    }
    //修改发当前用户
    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {

        PreparedStatement preparedStatement = null;
        int rows = 0;
        if(connection!=null){
            Object[] params = {password,id};
            String sql ="update store_user set userPassword = ? where id = ?";
            rows = BaseDao.executeDml(connection, sql, params, preparedStatement);
            BaseDao.closeResources(null,preparedStatement,null);
        }
        return rows;
    }

    //查询用户总数
    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count =0;


        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT count(1) as count from store_user,store_role where store_role.id = store_user.userRole");
            ArrayList list = new ArrayList();  //存放我们的参数

            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" AND store_user.userName like ?");
                list.add("%"+username+"%");

            }
            if(userRole>0){
                sql.append(" AND store_user.userRole = ?");
                list.add(userRole);
            }

            Object[] params = list.toArray();
            ResultSet execute = BaseDao.execute(connection, sql.toString(), params, resultSet, preparedStatement);
            if(execute.next()){
                count = execute.getInt("count");
            }
            BaseDao.closeResources(null,preparedStatement,resultSet);
        }
        return count;
    }

    //获取用户列表
    @Override
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from store_user u,store_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            //在mysql数据库中，分页使用 limit startIndex，pageSize ; 总数
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql : " + sql.toString());
            resultSet = BaseDao.execute(connection,sql.toString(),params,resultSet,preparedStatement );
            while (resultSet.next()) {
                User _user = new User();
                _user.setId(resultSet.getInt("id"));
                _user.setUserCode(resultSet.getString("userCode"));
                _user.setUserName(resultSet.getString("userName"));
                _user.setGender(resultSet.getInt("gender"));
                _user.setBirthday(resultSet.getDate("birthday"));
                _user.setPhone(resultSet.getString("phone"));
                _user.setUserRole(resultSet.getInt("userRole"));
                _user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResources(null,preparedStatement, resultSet);
        }
        return userList;
    }

    //增加用户信息
    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if (null != connection) {
            String sql = "insert into store_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getUserRole(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
            updateRows = BaseDao.executeDml(connection,sql, params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return updateRows;
    }

    //通过userId获取用户
    @Override
    public User getUserById(Connection connection, String id) throws Exception {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (null != connection) {
            String sql = "select u.*,r.roleName as userRoleName from store_user u,store_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};
            resultSet = BaseDao.execute(connection,sql,params,resultSet,preparedStatement);
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getTimestamp("modifyDate"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        System.out.println(user);
        return user;
    }

    //删除用户
    @Override
    public int deleteUserById(Connection connection, Integer delId) throws Exception  {
        PreparedStatement preparedStatement = null;
        int flag = 0;
        if (null != connection) {
            String sql = "delete from store_user where id=?";
            Object[] params = {delId};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null,preparedStatement, null);
        }
        return flag;
    }

    //修改用户信息
    @Override
    public int modify(Connection connection, User user) throws Exception {
        int flag = 0;
        PreparedStatement preparedStatement = null;
        if (null != connection) {
            String sql = "update store_user set userName=?," +
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                    user.getModifyDate(), user.getId()};
            flag = BaseDao.executeDml(connection,sql, params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return flag;
    }
}
