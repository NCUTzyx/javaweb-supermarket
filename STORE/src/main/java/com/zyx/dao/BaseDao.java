package com.zyx.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author 张宇森
 * @version 1.0
 */
//操作数据库的基本类
@SuppressWarnings("all")
public class BaseDao {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;


    //读取配置文件
    //静态代码块 => 类加载的时候初始化
    static {
        Properties properties = new Properties();
        //通过类加载器去读取对应的资源
        ClassLoader loader = BaseDao.class.getClassLoader();
        InputStream resource = loader.getResourceAsStream("zyx.properties");
        try {
            properties.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    //获取数据库的连接
    public static Connection getConnection(){

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    //查询基本方法
    public static ResultSet execute(Connection connection,String sql,Object[] params,ResultSet resultSet,PreparedStatement preparedStatement) throws SQLException {

            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i+1,params[i]);
            }

            resultSet = preparedStatement.executeQuery();
            return resultSet;

    }
    //增删改基本方法
    public static int executeDml(Connection connection,String sql,Object[] params,PreparedStatement preparedStatement) throws SQLException {

            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i+1,params[i]);
            }
            int row = preparedStatement.executeUpdate();
            return row;

    }
    //关闭资源的基本方法
    public static boolean closeResources(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){

        boolean flag = true;
        if(resultSet!=null){
            try {
                resultSet.close();
                //GC回收
                resultSet =null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement =null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;

            }
        }

        if(connection!=null){
            try {
                connection.close();
                //GC回收
                connection =null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

}
