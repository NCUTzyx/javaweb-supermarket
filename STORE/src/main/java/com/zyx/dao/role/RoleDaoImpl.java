package com.zyx.dao.role;

import com.zyx.dao.BaseDao;
import com.zyx.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 张宇森
 * @version 1.0
 */
@SuppressWarnings("all")
public class RoleDaoImpl implements RoleDao{

    //获取角色列表
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {


        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList list = new ArrayList();
        if(connection!=null){
            String sql = "SELECT * FROM store_role";
            Object[] params = {};
            resultSet = BaseDao.execute(connection, sql, params, resultSet, preparedStatement);
            while(resultSet.next()){
                Role role = new Role();
                int id = resultSet.getInt("id");
                String roleCode = resultSet.getString("roleCode");
                String roleName = resultSet.getString("roleName");
                role.setId(id);
                role.setRoleName(roleName);
                role.setRoleCode(roleCode);
                list.add(role);
            }
            BaseDao.closeResources(null,preparedStatement,resultSet);
        }
        return list;
    }
}
