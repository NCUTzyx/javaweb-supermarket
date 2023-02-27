package com.zyx.service.provider;

import com.zyx.dao.BaseDao;
import com.zyx.dao.bill.BillDao;
import com.zyx.dao.bill.BillDaoImpl;
import com.zyx.dao.provider.ProviderDao;
import com.zyx.dao.provider.ProviderDaoImpl;
import com.zyx.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class ProviderServiceImpl implements ProviderService {

    private ProviderDao providerDao;
    private BillDao billDao;

    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
        billDao = new BillDaoImpl();
    }

    //增加供应商
    @Override
    public boolean add(Provider provider) {
        // TODO Auto-generated method stub
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            if (providerDao.add(connection, provider) > 0)
                flag = true;
            connection.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } finally {
            //在service层进行connection连接的关闭
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    //获取供应商列表
    @Override
    public List<Provider> getProviderList(String proName, String proCode) {
        // TODO Auto-generated method stub
        Connection connection = null;
        List<Provider> providerList = null;
        System.out.println("query proName ---- > " + proName);
        System.out.println("query proCode ---- > " + proCode);
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proName, proCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return providerList;
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     * <p>
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */

    //删除供应商
    @Override
    public int deleteProviderById(String delId) {
        Connection connection = null;
        int billCount = -1;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(connection, delId);
            if (billCount == 0) {
                providerDao.deleteProviderById(connection, delId);
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return billCount;
    }

    //获取供应商
    @Override
    public Provider getProviderById(String id) {
        Provider provider = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            provider = null;
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return provider;
    }

    //修改用户信息
    @Override
    public boolean modify(Provider provider) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (providerDao.modify(connection, provider) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return flag;
    }

}
