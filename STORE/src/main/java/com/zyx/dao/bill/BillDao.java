package com.zyx.dao.bill;

import com.zyx.pojo.Bill;

import java.sql.Connection;
import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface BillDao {

    //增加订单
    public int add(Connection connection, Bill bill) throws Exception;


    //获取供应商列表
    public List<Bill> getBillList(Connection connection, Bill bill) throws Exception;

    //删除订单
    public int deleteBillById(Connection connection, String delId) throws Exception;


    //获取订单
    public Bill getBillById(Connection connection, String id) throws Exception;

    //修改订单信息
    public int modify(Connection connection, Bill bill) throws Exception;

    //根据供应商ID查询订单数量
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception;

}
