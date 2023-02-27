package com.zyx.dao.provider;

import com.zyx.pojo.Provider;

import java.sql.Connection;
import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface ProviderDao {

    //增加供应商
    public int add(Connection connection, Provider provider) throws Exception;

    //获取供应商列表
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception;

    //删除供应商
    public int deleteProviderById(Connection connection, String delId) throws Exception;

    //获取供应商
    public Provider getProviderById(Connection connection, String id) throws Exception;

    //修改供应商信息
    public int modify(Connection connection, Provider provider) throws Exception;

}
