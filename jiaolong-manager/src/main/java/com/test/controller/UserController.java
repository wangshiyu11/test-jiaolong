package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.test.ResponseResult;
import com.test.entity.User;
import com.test.exception.LoginException;
import com.test.jwt.JWTUtils;
import com.test.randm.VerifyCodeUtils;
import com.test.service.UserService;
import com.test.utils.MD5;
import com.test.utils.UID;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ：
 * @description :TODO
 * @date ：Created in 2019/8/5 14:24
 */
@Controller
public class UserController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;
    String addr;
    /**
     * 获取用户列表
     */
    @RequestMapping("userByList")
    @ResponseBody
    public PageInfo<User> userList(@RequestBody Map<String,Object> map){
        System.out.println("-----------分页-------------");
        System.out.println("++++++++++++"+map.get("xingbie"));
        PageInfo<User> page1 = userService.userList(
                Integer.valueOf(map.get("page").toString()),
                Integer.valueOf(map.get("pageSize").toString()),
                map.get("name").toString(),
                map.get("dt1").toString(),
                map.get("dt2").toString(),
                map.get("xingbie").toString());
        return page1;
    }

    /**
     * 上传图片
     */

    @PostMapping("addPic")
    @ApiOperation("文件上传的接口")
    public void addPic(@Param("file")MultipartFile file) throws IOException {

        System.out.println("进入图片方法");
        String imgUrl = "F:\\tu\\" + file.getOriginalFilename();
        File file2 = new File(imgUrl);

        //可自定义大小
        Thumbnails.of(file2).scale(0.25f).toFile(file2.getAbsolutePath()+"_25.jpg");
        System.out.println("图片的方法===="+file2.getAbsolutePath()+"_25.jpg");
        this.addr =file.getOriginalFilename();
    }


    /**
     * 用户删除
     */
    @RequestMapping("del")
    @ResponseBody
    public void del(@RequestBody User user){
        userService.del(user.getId());
    }

    /**
     * 用户添加
     */
    @RequestMapping("add")
    @ResponseBody
    public int add(@RequestBody User user){
        System.out.println("----------"+user);
        if(user!=null){
            String password = MD5.encryptPassword(user.getPassword(), "lcg");
            user.setPassword(password);
            System.out.println(password);
        }else{
            System.out.println("密码保存错误");
        }
        int i=userService.add(user);
        return i;
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public int update(@RequestBody User user){
        int i = userService.update(user);
        return i;
    }

    @RequestMapping("findByloginName")
    @ResponseBody
    public int  findByloginName(@RequestBody Map<String,Object> map){
        System.out.println(map.get("loginName").toString());
        int i=userService.findByloginName(map.get("loginName").toString());
        return i;
    }

}
