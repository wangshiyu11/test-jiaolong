package com.test.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.MenuDao;
import com.test.dao.RoleDao;
import com.test.dao.UserDao;
import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import com.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:23
 */
@Component
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    MenuDao menuDao;

    public User login(String loginName) {
        User user = userDao.findByLoginName(loginName);
        if (user != null) {
            RoleInfo roleInfoByUser = roleDao.forRoleInfoByUserId(user.getId());
            user.setRoleInfo(roleInfoByUser);
            if (roleInfoByUser != null) {
                List<MenuInfo> menuInfoByRole = menuDao.getFirstMenuInfo(roleInfoByUser.getId(), 1,0L);
                Map<String, String> authMap = new Hashtable<>();
                this.getForMenuInfo(menuInfoByRole,roleInfoByUser.getId(),authMap);
                //设置菜单的子权限
                user.setAuthmap(authMap);
                user.setMenuInfoList(menuInfoByRole);
            }
        }
        return user;
    }



    /**
     * 获取子权限的递归方法
     * @param firstMenuInfo
     * @param roleId
     */
    public void getForMenuInfo(List<MenuInfo> firstMenuInfo,Long roleId,Map<String,String> authMap) {

        for (MenuInfo menuInfo : firstMenuInfo) {
            int leval = menuInfo.getLeval() + 1;
            //获取下级的菜单信息
            List<MenuInfo> firstMenuInfo1 = menuDao.findMenuInfoByRole(roleId, leval,menuInfo.getId());
            if (firstMenuInfo1 != null) {

                //整理后台的数据访问链接
                if (leval == 4) {
                    for (MenuInfo menu : firstMenuInfo1) {
                        authMap.put(menu.getUrl(), "");
                    }
                }
                //设置查出来的菜单到父级对象中
                menuInfo.setMenuInfoList(firstMenuInfo1);
                //根据查出来的下级菜单继续查询该菜单包含的子菜单
                getForMenuInfo(firstMenuInfo1, roleId, authMap);
            } else {
                break;
            }
        }
    }



}

