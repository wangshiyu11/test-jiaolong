package com.test.service;

import com.test.dao.MenuDao;
import com.test.dao.RoleDao;
import com.test.dao.UserDao;
import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import com.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User login(String loginName){
        User user = userDao.findbyLoginName(loginName);
        if(user!=null){
            RoleInfo roleInfoByUser = roleDao.findRoleInfoByUser(user.getId());
            user.setRoleInfo(roleInfoByUser);
            if(roleInfoByUser!=null){
                List<MenuInfo> menuInfoByRole = menuDao.findMenuInfoByRole(roleInfoByUser.getId(), 1);
                Map<String,String> map=new Hashtable<>();
            }
        }
        return user;
    }

}
