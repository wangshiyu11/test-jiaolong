package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.test.ResponseResult;
import com.test.entity.User;
import com.test.exception.LoginException;
import com.test.jwt.JWTUtils;
import com.test.randm.VerifyCodeUtils;
import com.test.service.UserService;
import com.test.utils.MD5;
import com.test.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String,Object> map) throws LoginException {
        ResponseResult responseResult=ResponseResult.getResponseResult();
        //获取生成的验证码
        String code = redisTemplate.opsForValue().get(map.get("codekey").toString());
        //获取传入的验证码是否是生成后存在redis中的验证码
        if(code==null||!code.equals(map.get("code").toString()) ){
            responseResult.setCode(500);
            responseResult.setError("验证码错误,请重新刷新页面登陆");
            return responseResult;
        }
        //进行用户密码的校验
        if(map!=null&&map.get("loginname")!=null){
            //根据用户名获取用户信息
            System.out.println(map.get("loginname").toString());
            System.out.println(map.get("password").toString());
            User user = userService.login(map.get("loginname").toString());
            if(user!=null){
                //比对密码
                String password = MD5.encryptPassword(map.get("password").toString(), "lcg");
                System.out.println(password);
                System.out.println(user.getPassword());
                if(user.getPassword().equals(password)){

                    //将用户信息转存为JSON串
                    String userinfo = JSON.toJSONString(user);

                    //将用户信息使用JWt进行加密，将加密信息作为票据
                    String token = JWTUtils.generateToken(userinfo);

                    //将加密信息存入statuInfo
                    responseResult.setToken(token);

                    //将生成的token存储到redis库
                    redisTemplate.opsForValue().set("USERINFO"+user.getId().toString(),token);
                    //将该用户的数据访问权限信息存入缓存中
                    redisTemplate.delete("USERDATAAUTH"+user.getId().toString());
                    redisTemplate.opsForHash().putAll("USERDATAAUTH"+user.getId().toString(),user.getAuthmap());

                    //设置token过期 30分钟
                    redisTemplate.expire("USERINFO"+user.getId().toString(),600,TimeUnit.SECONDS);
                    //设置返回值
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    //设置成功信息
                    responseResult.setSuccess("登陆成功！^_^");
                    System.out.println("封装的返回值结果"+responseResult);
                    return responseResult;
                }else{
                    throw new LoginException("用户名或密码错误");
                }
            }else{
                throw new LoginException("用户名或密码错误");
            }
        }else{
            throw new LoginException("用户名或密码错误");
        }

    }

    /**
     * 获取滑动验证码
     */
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String code = VerifyCodeUtils.generateVerifyCode(5);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(code);
        String uidcode = "CODE" + UID.getUUID16();
        redisTemplate.opsForValue().set(uidcode,code);
        Cookie cookie = new Cookie("authcode", uidcode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return responseResult;
    }

    /**
     * 手动加载密码
     */
    public static void main(String[] args) {
        System.out.println(MD5.encryptPassword("123456","lcg"));
        JSONObject jsonObject = JWTUtils.decodeJwtTocken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNTY0OTEzMzIzMzU4LCJleHAiOjE1NjQ5MTMzODMsInVzZXJpbmZvIjoie1wiaWRcIjpcIjY0NTU2NDY1NFwifSJ9.iT-NmNBkbjK29t4DLtyJvsAwp770QyYkUpEGB-Lmy-xDVH2NWUtPqQJmovV7PZV46IGPVUMvYMOAaEhbJ6voaA");
        System.out.println(jsonObject.get("id"));
    }

    //退出登录
    @RequestMapping("loginout")
    @ResponseBody
    public ResponseResult loginout(@RequestBody Map<String,Object> map){
        //   Claim claims= (Claim) request.getAttribute("userinfo");
        //清除用户信息
        redisTemplate.delete("USERINFO"+map.get("id").toString());
        ResponseResult responseResult=ResponseResult.getResponseResult();
        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;
    }

}
