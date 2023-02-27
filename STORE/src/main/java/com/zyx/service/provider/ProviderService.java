package com.zyx.service.provider;

import com.zyx.pojo.Provider;

import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface ProviderService {


    //增加供应商
    public boolean add(Provider provider);

    //获取供应商列表
    public List<Provider> getProviderList(String proName, String proCode);

    //删除供应商
    public int deleteProviderById(String delId);

    //获取供应商
    public Provider getProviderById(String id);

    //修改供应商信息
    public boolean modify(Provider provider);

}
