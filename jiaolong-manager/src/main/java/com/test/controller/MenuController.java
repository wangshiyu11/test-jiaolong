package com.test.controller;

import com.test.ResponseResult;
import com.test.dao.MenuDao;
import com.test.entity.MenuInfo;
import com.test.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/9 19:26
 */
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping("menuList")
    @ApiOperation("根据角色id菜单列表")
    public List<MenuInfo> menuList(@RequestBody Map<String,Object> map ){
        List<MenuInfo> list= menuService.findMenu(Long.valueOf(map.get("roleid").toString()));
        return list;
    }

    @RequestMapping("menuByList")
    @ApiOperation("菜单列表")
    public List<MenuInfo> menuByList(){
        List<MenuInfo> list= menuService.menuByList();
        return list;
    }

    @RequestMapping("insertMenu")
    @ResponseBody
    @ApiOperation("添加菜单")
    public ResponseResult insertMenu(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        menuService.insertMenu(
                Integer.valueOf(map.get("id").toString()),
                Integer.valueOf(map.get("leval").toString()),
                map.get("menuName").toString(),
                map.get("url").toString()
        );
        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;

    }

    @RequestMapping("deleteMenu")
    @ResponseBody
    @ApiOperation("删除菜单")
    public ResponseResult deleteMenu(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        MenuInfo menuByRole = menuService.findMenuByRole(Integer.valueOf(map.get("id").toString()));
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
    }

    @RequestMapping("updateMenu")
    @ResponseBody
    @ApiOperation("修改菜单")
    public ResponseResult updateMenu(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        menuService.updateMenu(
                Integer.valueOf(map.get("id").toString()),
                map.get("menuName").toString(),
                map.get("url").toString()
        );
        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;
    }


}
