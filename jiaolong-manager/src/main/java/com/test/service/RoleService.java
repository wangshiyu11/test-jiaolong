package com.test.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.MenuDao;
import com.test.dao.RoleDao;
import com.test.entity.RoleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/9 7:56
 */
@Service
public class RoleService {

    @Autowired
    RoleDao roleDao;
    @Autowired
    MenuDao menuDao;

    public int bangdingRole(Integer userId, Integer roleId) {
        return roleDao.bangdingRole(userId,roleId);
    }

    public List<RoleInfo> liebiaoRole() {
        return roleDao.liebiaoRole();
    }

    public PageInfo<RoleInfo> roleByPage(Integer page, Integer pageSize,String name) {
        PageHelper.startPage(page,pageSize);
        List<RoleInfo> list = roleDao.roleByPage(name);
        for (RoleInfo r:list
             ) {
                r.setMenuMids(menuDao.rolefindByMenu(r.getId()));
        }
        return new PageInfo<>(list);
    }

    public int addRole(RoleInfo roleInfo) {
        return roleDao.addRole(roleInfo);
    }

    public void delRole(Long id) {
        roleDao.delRole(id);
    }

    public void delUserRole(Long id) {
        roleDao.delUserRole(id);
    }

    public void delMenuByRoleId(Long id) {
        roleDao.delMenuByRoleId(id);
    }

    public void updateRole(String roleName, String miaoShu,Long id) {
        roleDao.updateRole(roleName,miaoShu,id);
    }

    public void insertMenuByRoleId(long id, String[] ids) {
        roleDao.insertMenuByRoleId(id,ids);
    }

    public void delUserByRole(Long userId) {
        roleDao.delUserByRole(userId);
    }

    public RoleInfo findRoleByUser(Integer id) {
        return roleDao.findRoleByUser(id);
    }
}
