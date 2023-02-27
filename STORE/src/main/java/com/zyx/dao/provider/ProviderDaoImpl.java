package com.zyx.dao.provider;

import com.mysql.jdbc.StringUtils;
import com.zyx.dao.BaseDao;
import com.zyx.pojo.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class ProviderDaoImpl implements ProviderDao {

    //增加供应商
    @Override
    public int add(Connection connection, Provider provider) throws Exception {

        PreparedStatement preparedStatement = null;
        int flag = 0;
        if (null != connection) {
            String sql = "insert into store_provider (proCode,proName,proDesc," +
                    "proContact,proPhone,proAddress,proFax,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate()};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null,preparedStatement, null);
        }
        return flag;
    }

    //获取供应商列表
    @Override
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Provider> providerList = new ArrayList<Provider>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select * from store_provider where 1=1 ");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(proName)) {
                sql.append(" and proName like ?");
                list.add("%" + proName + "%");
            }
            if (!StringUtils.isNullOrEmpty(proCode)) {
                sql.append(" and proCode like ?");
                list.add("%" + proCode + "%");
            }
            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            resultSet = BaseDao.execute(connection,sql.toString(), params,resultSet,preparedStatement);
            while (resultSet.next()) {
                Provider _provider = new Provider();
                _provider.setId(resultSet.getInt("id"));
                _provider.setProCode(resultSet.getString("proCode"));
                _provider.setProName(resultSet.getString("proName"));
                _provider.setProDesc(resultSet.getString("proDesc"));
                _provider.setProContact(resultSet.getString("proContact"));
                _provider.setProPhone(resultSet.getString("proPhone"));
                _provider.setProAddress(resultSet.getString("proAddress"));
                _provider.setProFax(resultSet.getString("proFax"));
                _provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                providerList.add(_provider);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return providerList;
    }

    //删除供应商
    @Override
    public int deleteProviderById(Connection connection, String delId) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement preparedStatement= null;
        int flag = 0;
        if (null != connection) {
            String sql = "delete from store_provider where id=?";
            Object[] params = {delId};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null,preparedStatement, null);
        }
        return flag;
    }

    //获取供应商
    @Override
    public Provider getProviderById(Connection connection, String id) throws Exception {
        // TODO Auto-generated method stub
        Provider provider = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (null != connection) {
            String sql = "select * from store_provider where id=?";
            Object[] params = {id};
            resultSet =  BaseDao.execute(connection,sql, params,resultSet,preparedStatement);
            if (resultSet.next()) {
                provider = new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setCreatedBy(resultSet.getInt("createdBy"));
                provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                provider.setModifyBy(resultSet.getInt("modifyBy"));
                provider.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return provider;
    }

    //修改供应商信息
    @Override
    public int modify(Connection connection, Provider provider) throws Exception {
        int flag = 0;
        PreparedStatement preparedStatement = null;
        if (null != connection) {
            String sql = "update store_provider set proName=?,proDesc=?,proContact=?," +
                    "proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {provider.getProName(), provider.getProDesc(), provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getModifyBy(), provider.getModifyDate(), provider.getId()};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null,preparedStatement, null);
        }
        return flag;
    }

}
