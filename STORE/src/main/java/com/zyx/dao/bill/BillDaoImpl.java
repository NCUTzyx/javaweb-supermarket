package com.zyx.dao.bill;

import com.mysql.jdbc.StringUtils;
import com.zyx.dao.BaseDao;
import com.zyx.pojo.Bill;

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
public class BillDaoImpl implements BillDao {

    //增加订单
    @Override
    public int add(Connection connection, Bill bill) throws Exception {

        PreparedStatement preparedStatement = null;
        int flag = 0;
        if (null != connection) {
            String sql = "insert into store_bill (billCode,productName,productDesc," +
                    "productUnit,productCount,totalPrice,isPayment,providerId,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getProviderId(), bill.getCreatedBy(), bill.getCreationDate()};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
            System.out.println("dao--------flag " + flag);
        }
        return flag;
    }

    //获取供应商列表
    @Override
    public List<Bill> getBillList(Connection connection, Bill bill) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Bill> billList = new ArrayList<Bill>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select b.*,p.proName as providerName from store_bill b, store_provider p where b.providerId = p.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(bill.getProductName())) {
                sql.append(" and productName like ?");
                list.add("%" + bill.getProductName() + "%");
            }
            if (bill.getProviderId() > 0) {
                sql.append(" and providerId = ?");
                list.add(bill.getProviderId());
            }
            if (bill.getIsPayment() > 0) {
                sql.append(" and isPayment = ?");
                list.add(bill.getIsPayment());
            }
            Object[] params = list.toArray();
            System.out.println("sql --------- > " + sql.toString());
            resultSet = BaseDao.execute(connection, sql.toString(), params,resultSet,preparedStatement);
            while (resultSet.next()) {
                Bill _bill = new Bill();
                _bill.setId(resultSet.getInt("id"));
                _bill.setBillCode(resultSet.getString("billCode"));
                _bill.setProductName(resultSet.getString("productName"));
                _bill.setProductDesc(resultSet.getString("productDesc"));
                _bill.setProductUnit(resultSet.getString("productUnit"));
                _bill.setProductCount(resultSet.getBigDecimal("productCount"));
                _bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                _bill.setIsPayment(resultSet.getInt("isPayment"));
                _bill.setProviderId(resultSet.getInt("providerId"));
                _bill.setProviderName(resultSet.getString("providerName"));
                _bill.setCreationDate(resultSet.getTimestamp("creationDate"));
                _bill.setCreatedBy(resultSet.getInt("createdBy"));
                billList.add(_bill);
            }
            BaseDao.closeResources(null,preparedStatement, resultSet);
        }
        return billList;
    }

    //删除订单
    @Override
    public int deleteBillById(Connection connection, String delId) throws Exception {

        PreparedStatement preparedStatement = null;
        int flag = 0;
        if (null != connection) {
            String sql = "delete from store_bill where id=?";
            Object[] params = {delId};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return flag;
    }

    //获取订单
    @Override
    public Bill getBillById(Connection connection, String id) throws Exception {
        Bill bill = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (null != connection) {
            String sql = "select b.*,p.proName as providerName from store_bill b, store_provider p " +
                    "where b.providerId = p.id and b.id=?";
            Object[] params = {id};
            resultSet = BaseDao.execute(connection, sql, params,resultSet,preparedStatement);
            if (resultSet.next()) {
                bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductDesc(resultSet.getString("productDesc"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setProviderId(resultSet.getInt("providerId"));
                bill.setProviderName(resultSet.getString("providerName"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            BaseDao.closeResources(null,preparedStatement, resultSet);
        }
        return bill;
    }

    //修改订单信息
    @Override
    public int modify(Connection connection, Bill bill) throws Exception {
        int flag = 0;
        PreparedStatement preparedStatement = null;
        if (null != connection) {
            String sql = "update store_bill set productName=?," +
                    "productDesc=?,productUnit=?,productCount=?,totalPrice=?," +
                    "isPayment=?,providerId=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getProviderId(), bill.getModifyBy(), bill.getModifyDate(), bill.getId()};
            flag = BaseDao.executeDml(connection, sql, params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        return flag;
    }

    //根据供应商ID查询订单数量
    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception {
        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (null != connection) {
            String sql = "select count(1) as billCount from store_bill where" +
                    "	providerId = ?";
            Object[] params = {providerId};
            resultSet = BaseDao.execute(connection, sql, params,resultSet,preparedStatement);
            if (resultSet.next()) {
                count = resultSet.getInt("billCount");
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return count;
    }
}
