package com.test.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.UserDao;
import com.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:23
 */
@Component
public class UserService {

    @Autowired
    UserDao userDao;

    /**
     *列表
     * @return
     */
    public PageInfo<User> userList(Integer page, Integer pageSize, String name, String dt1, String dt2, String xingbie) {
        PageHelper.startPage(page,pageSize);
        List<User>list=userDao.userList(name,dt1,dt2,xingbie);
        return new PageInfo<>(list);
    }

    /**
     * 根据id删除用户信息
     * @param id
     */
    public void del(Long id) {
        userDao.del(id);
    }


    public int add(User user) {
        return userDao.add(user);
    }


    public int update(User user) {
        return userDao.update(user);
    }

    public int findByloginName(String loginName) {
         return userDao.findByloginName(loginName);
    }

    public User findUserByRole(Integer id) {
        return userDao.findUserByRole(id);
    }
}

