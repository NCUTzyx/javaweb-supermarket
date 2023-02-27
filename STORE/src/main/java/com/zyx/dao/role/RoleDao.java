package com.zyx.dao.role;

import com.zyx.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public interface RoleDao {

    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
