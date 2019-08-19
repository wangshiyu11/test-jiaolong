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
import com.test.util.EmailUtil;
import com.test.utils.MD5;
import com.test.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    //获取当前时间
                    String format = sdf.format(new Date());
                    //自增1
                    redisTemplate.opsForHash().increment("number",format,1l);
                    //获取集合大小
                    int length = redisTemplate.opsForList().range("date", 0, -1).size();
                    //获取集合最后一个元素
                    String date = redisTemplate.opsForList().index("date", length-1);
                    if(date == null){
                        redisTemplate.opsForList().leftPush("date",format);
                    }else{
                        if(date.equals(format)){

                            redisTemplate.opsForList().rightPop("date");

                            redisTemplate.opsForList().rightPush("date",format);

                        }else{
                            redisTemplate.opsForList().rightPush("date",format);
                        }
                    }
                    return  responseResult;
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
     * 利用短信验证码进行登录
     */
    @RequestMapping("loginByTel")
    @ResponseBody
    public ResponseResult loginByTel(@RequestBody Map<String,Object> map)throws LoginException{
        ResponseResult responseResult=ResponseResult.getResponseResult();
        //获取生成的验证码
        System.out.println(map.get("authcode").toString()+"*****");
        String phone = redisTemplate.opsForValue().get("phone");
        //获取传入的验证码是否是生成后存在redis中的验证码
        if(phone==null||!phone.equals(map.get("authcode").toString()) ){
            responseResult.setCode(500);
            responseResult.setError("验证码错误,请重新刷新页面登陆");
            return responseResult;
        }
       if(map.get("tel")!=null){
           User user = userService.loginByTel(map.get("tel").toString());
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
           throw new LoginException("登录错误");
       }

    }

    //获取手机验证码
    @RequestMapping("getPhoneCode")
    @ResponseBody
    public ResponseResult getPhoneCode(@RequestBody Map<String,String> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String code = userService.getPhoneCode(map.get("tel"));
        redisTemplate.opsForValue().set("phone",code);
        redisTemplate.expire("phone",500,TimeUnit.SECONDS);
        responseResult.setCode(200);
        return responseResult;
    }

    //判断用户是否存在
    @RequestMapping("findUsername")
    @ResponseBody
    public ResponseResult findUsername(@RequestBody Map<String,String>map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        User user=userService.findUsername(map.get("username"));
        System.out.println("//"+map.get("username"));
        if(user!=null){
            System.out.println("------//---");
            responseResult.setSuccess("ok");
            responseResult.setCode(200);
            return responseResult;
        }else{
            responseResult.setError("用户不存在");
            responseResult.setCode(500);
            return responseResult;
        }
    }


    //获取邮箱验证码
    @RequestMapping("getEmailCode")
    @ResponseBody
    public ResponseResult getEmailCode(@RequestBody Map<String,String> map) throws MessagingException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        User user = userService.getEmailCode(map.get("email"));
        String email = map.get("email");
        if(user!=null){
            int num = (int) ((Math.random() * 9 + 1) * 100000);

            EmailUtil.sendEmail(email,String.valueOf(num));

            redisTemplate.opsForValue().set("email",String.valueOf(num));
            redisTemplate.expire(email,500,TimeUnit.SECONDS);
            responseResult.setCode(200);
            return responseResult;

        }else{
            responseResult.setError("邮箱不存在");
            responseResult.setCode(500);
            return responseResult;
        }
    }

    //判断邮箱验证码是否与redis中相同
    @RequestMapping("setEmailCode")
    @ResponseBody
    public ResponseResult setEmailCode(@RequestBody Map<String,String> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //获取生成的验证码
        System.out.println(map.get("authcode")+"*****");
        String email = redisTemplate.opsForValue().get("email");
        if(email.equals(map.get("authcode")) ){
            responseResult.setCode(200);
            return responseResult;
        }else{
            responseResult.setCode(500);
            return responseResult;
        }
    }


    /**
     * 修改用户密码
     */
    @RequestMapping("updatePassword")
    @ResponseBody
    public ResponseResult updatePassword(@RequestBody Map<String,String> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String password = MD5.encryptPassword(map.get("pwd"), "lcg");
        System.out.println("++/"+password);
        System.out.println("/*-+"+map.get("username"));
        userService.updatePassword(password,map.get("username"));

        responseResult.setSuccess("ok");
        responseResult.setCode(200);
        return responseResult;
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


    //折线图
    @RequestMapping("selectzhexian")
    @ResponseBody
    public ResponseResult selectzhexian(){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        Map<String,Object> map=new HashMap<>();
        //通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列
        List<String> date = redisTemplate.opsForList().range("date", 0, -1);
        if(date!=null){
            List<Object> num = new ArrayList<>();
            map.put("date",date);
            for (String date1: date) {
                Object number = redisTemplate.opsForHash().get("number", date1);
                num.add(number);
            }
            map.put("num",num);
        }
        responseResult.setResult(map);
        return responseResult;
    }

}
