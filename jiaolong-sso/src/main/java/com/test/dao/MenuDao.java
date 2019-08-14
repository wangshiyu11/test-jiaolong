package com.test.dao;

import com.test.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends JpaRepository<MenuInfo,Long> {
    /**
     * 获取角色的菜单信息
     * @return
     */
    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm ON brm.menuId=bm.id where brm.roleId=?1 and bm.leval=?2 and bm.parentId=?3 ",nativeQuery = true)
    public List<MenuInfo> findMenuInfoByRole(Long roleId, Integer leval,Long parentId);

    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm ON brm.menuId=bm.id where brm.roleId=?1 and bm.leval=?2 and bm.parentId=?3 ",nativeQuery = true)
    public List<MenuInfo> getFirstMenuInfo(Long roleId, Integer leval,Long parentId);















   /*@Query(value = "SELECT bm.* from base_role_menu brm INNER JOIN base_menu bm on brm.menuId=bm.id where bm.leval=?1 and bm.parentId=?2",nativeQuery = true)
    public List<MenuInfo> findChildMenu(Integer leval,Integer parentId);

    @Query(value = "select * from base_menu where leval=?1",nativeQuery = true)
    List<MenuInfo> findMenu(int i);*/

}
