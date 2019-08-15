package com.test.controller;

import com.github.pagehelper.PageInfo;
import com.test.ResponseResult;
import com.test.entity.MenuInfo;
import com.test.entity.RoleInfo;
import com.test.entity.User;
import com.test.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/9 7:56
 */
@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 下拉框角色
     * @return
     */
    @RequestMapping("liebiaoRole")
    public List<RoleInfo> liebiaoRole(){
        List<RoleInfo> list=roleService.liebiaoRole();
        return list;
    }

    /**
     * 用户绑定角色
     * @param map
     * @return
     */
    @RequestMapping("bangdingRole")
    public int  bangdingRole(@RequestBody Map<String,Object> map){
        System.out.println("---------------"+map.get("userId"));
        System.out.println("-----------"+map.get("roleId"));
        //根据roleid删除中间表中的数据
        roleService.delUserRole(Long.valueOf(map.get("roleIds").toString()));
        //根据userid删除中间表中的数据
        roleService.delUserByRole(Long.valueOf(map.get("userId").toString()));
        int i=roleService.bangdingRole(Integer.valueOf(map.get("userId").toString()),
                Integer.valueOf(map.get("roleIds").toString()));
        return i;
    }

    /**
     * 带分页的角色列表
     */
    @RequestMapping("roleByPage")
    public PageInfo<RoleInfo> roleByPage(@RequestBody  Map<String,Object> map){
        PageInfo<RoleInfo> pageInfo=roleService.roleByPage(
                Integer.valueOf(map.get("page").toString()),
                Integer.valueOf(map.get("pageSize").toString()),
                map.get("name").toString());

        //点击角色管理 直接查询当前用户所对应的角色的leval和id

        return pageInfo;
    }

    @RequestMapping("selectRoleByUser")
    public RoleInfo selectRoleByUser(@RequestBody Map<String,Object> map){
        RoleInfo roleInfo=roleService.selectRoleByUser(Long.valueOf(map.get("id").toString()));
        System.out.println("///***0"+roleInfo);
        return roleInfo;
    }

    /**
     * 角色添加
     */
    @RequestMapping("addRole")
    public ResponseResult addRole(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        System.out.println("*********"+map);
        roleService.addRole(
                map.get("roleName").toString(),
                map.get("miaoShu").toString(),
                Integer.valueOf(map.get("leval").toString()),
                Integer.valueOf(map.get("parentId").toString())
        );
        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;
    }

    /**
     * 角色删除
     */
   /* @RequestMapping("delRole")
    public void delRole(@RequestBody Map<String,Object> map){
        System.out.println(map);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        RoleInfo roleInfo = roleService.findRoleByUser(Integer.valueOf(map.get("id").toString()));
        System.out.println("///////"+menuByRole.getRoleName());
        if(menuByRole.getRoleName()!=null){
            responseResult.setCode(500);
            responseResult.setResult(menuByRole.getRoleName());
        }else {
            menuService.deleteMenu(Long.valueOf(map.get("id").toString()));
            menuService.deleteMenuByRole(Integer.valueOf(map.get("id").toString()));
            responseResult.setSuccess("ok");
            responseResult.setCode(200);
        }
        return responseResult;
       *//* roleService.delRole(roleInfo.getId());
        roleService.delUserRole(roleInfo.getId());*//*
    }*/

    /**
     * 编辑角色权限
     */
    @RequestMapping("updateRole")
    public Object updateRole(@RequestBody Map<String,Object> map){
        System.out.println("//////"+map);
//        System.out.println("++++"+map.get("tree"));
//        System.out.println("++"+map.get("id"));
       long id = Long.parseLong(map.get("id").toString());
        String[] ids = map.get("ids").toString().split(",");
        roleService.updateRole(map.get("roleName").toString(),map.get("miaoShu").toString(),id);
        roleService.delMenuByRoleId(id);
        roleService.insertMenuByRoleId(id,ids);
        map.put("status",200);
        return map;
    }

}
