package com.zyx.service.role;

import com.zyx.dao.BaseDao;
import com.zyx.dao.role.RoleDao;
import com.zyx.dao.role.RoleDaoImpl;
import com.zyx.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class RoleServiceImpl implements RoleService {

    //引入Dao
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {

        List<Role> list = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
           list = roleDao.getRoleList(connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }

        return list;
    }

}
