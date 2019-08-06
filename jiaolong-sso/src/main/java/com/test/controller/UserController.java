package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.ResponseResult;
import com.test.entity.User;
import com.test.exception.LoginException;
import com.test.jwt.JWTUtils;
import com.test.randm.VerifyCodeUtils;
import com.test.service.UserService;
import com.test.utils.MD5;
import com.test.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseResult login(@RequestBody Map<String,Object> map) throws LoginException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String codekey = redisTemplate.opsForValue().get(map.get("codekey").toString());
        System.out.println(map.get("loginname"));
        System.out.println(codekey);
        if(codekey==null||!codekey.equals(map.get("code").toString())){
            responseResult.setCode(500);
            responseResult.setError("验证码错误,请重新刷新界面");
            return responseResult;
        }

        if(map!=null&&map.get("loginname")!=null){
            User user = userService.login(map.get("loginname").toString());
            if(user!=null){
                String s = MD5.encryptPassword(map.get("password").toString(), "lcg");
                if(user.getPassword().equals(s)){
                    String s1 = JSON.toJSONString(user);
                    String generateToken = JWTUtils.generateToken(s1);
                    responseResult.setToken(generateToken);
                    redisTemplate.opsForValue().set("User"+user.getId().toString(),generateToken);
                    //redisTemplate.opsForHash().putAll("UserAuth"+user.getId().toString(),user.getAuthmap());
                    redisTemplate.expire("User"+user.getId().toString(),600, TimeUnit.SECONDS);
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    responseResult.setSuccess("登陆成功");
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

}
