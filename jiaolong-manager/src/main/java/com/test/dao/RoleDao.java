package com.test.dao;

import com.test.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RoleDao {

    int bangdingRole(@Param(value = "userId") Integer userId, @Param(value = "roleId") Integer roleId);

    List<RoleInfo> liebiaoRole();

    List<RoleInfo> roleByPage(@Param(value = "name")String name);

    int addRole( RoleInfo roleInfo);

    void delRole(Long id);

    void delUserRole(Long id);

    void delMenuByRoleId(Long id);

    void updateRole(@Param(value = "roleName") String roleName,@Param(value = "miaoShu") String miaoShu,@Param(value = "id") Long id);

    void insertMenuByRoleId(@Param(value = "id")long id, @Param(value = "ids")String[] ids);

    void delUserByRole(@Param(value = "userId") Long userId);

    RoleInfo findRoleByUser(Integer id);
}
