package com.test.service;

import com.test.dao.MenuDao;
import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/9 15:09
 */
@Service
public class MenuService {

    @Autowired
    MenuDao menuDao;

    public List<MenuInfo> findMenu() {

        List<MenuInfo> menu =  menuDao.findMenu(1);
        this.getOtherMenu(menu);
        return  menu;

    }

    public void getOtherMenu(List<MenuInfo> menu){
        for (MenuInfo  menuInfo: menu) {
            List<MenuInfo> childMenu = menuDao.getChildMenu(menuInfo.getLeval() + 1, menuInfo.getId());
            System.out.println("child");
            childMenu.forEach(c->{
                System.out.println(c);
            });
            menuInfo.setMenuInfoList(childMenu);

            if(childMenu.size() > 0){
                this.getOtherMenu(childMenu);
            }
        }
    }

    public MenuInfo findMenuByRole(Integer id) {
        return menuDao.findMenuByRole(id);
    }

    public void deleteMenu(Long id) {
        menuDao.deleteMenu(id);
    }

    public void deleteMenuByRole(Integer id) {
        menuDao.deleteMenuByRole(id);
    }

    public void insertMenu(Integer id, Integer leval, String menuName, String url) {
        menuDao.insertMenu(id,leval+1,menuName,url);
    }

    public void updateMenu(Integer id, String menuName, String url) {
        menuDao.updateMenu(id,menuName,url);
    }
}
