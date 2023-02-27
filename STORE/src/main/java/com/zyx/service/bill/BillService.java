package com.zyx.service.bill;

import com.zyx.pojo.Bill;

import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface BillService {

    //增加订单
    public boolean add(Bill bill);


    //获取订单列表
    public List<Bill> getBillList(Bill bill);


    //删除订单
    public boolean deleteBillById(String delId);


    //获取订单
    public Bill getBillById(String id);


    //修改订单信息
    public boolean modify(Bill bill);

}
