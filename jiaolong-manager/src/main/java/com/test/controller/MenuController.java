package com.test.controller;

import com.test.ResponseResult;
import com.test.dao.MenuDao;
import com.test.entity.MenuInfo;
import com.test.service.MenuService;
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
    public List<MenuInfo> menuList(){
        List<MenuInfo> list= menuService.findMenu();
        return list;
    }

    @RequestMapping("insertMenu")
    @ResponseBody
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
