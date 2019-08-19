package com.test.service;

import com.test.dao.MenuDao;
import com.test.dao.RoleDao;
import com.test.dao.UserDao;
import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import com.test.entity.User;
import com.test.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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

    public String getPhoneCode(String tel){
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "039b2a2ca4c6453b8330b483996d5618";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        //随机生成验证码
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        querys.put("mobile", tel);
        querys.put("param", "code:"+num);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(num);
    }

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

    //根据电话查询
    public User loginByTel(String tel){
        User user = userDao.findUserByTel(tel);
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


    public User getEmailCode(String email) {
        User username=userDao.getEmailCode(email);
        return username;
    }

    public User findUsername(String username) {
        User username1 = userDao.findUsername(username);
        return username1;
    }

    public void updatePassword(String password,String username) {
        userDao.updatePassword(password,username);
    }
}

