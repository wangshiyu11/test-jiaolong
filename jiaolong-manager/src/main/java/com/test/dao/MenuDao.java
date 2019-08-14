package com.test.dao;

import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MenuDao {


    List<MenuInfo> findMenu(@Param(value = "leval") int i);

    List<MenuInfo> getChildMenu(@Param(value = "leval") int i,@Param(value = "parentId") Long id);

    List<Long> rolefindByMenu(Long id);

    MenuInfo findMenuByRole(Integer id);

    void deleteMenu(Long id);

    void deleteMenuByRole(Integer id);

    void insertMenu(@Param(value = "parentId") Integer id, @Param(value = "leval") Integer leval,@Param(value = "menuName") String menuName,@Param(value = "url") String url);

    void updateMenu(@Param(value = "id") Integer id, @Param(value = "menuName") String menuName,@Param(value = "url") String url);
}
