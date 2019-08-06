package com.test;

import lombok.Data;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 11:55
 */
@Data
public class ResponseResult {

    //返回的信息编码
    int code;

    //错误信息
    String error;

    //程序返回结果
    Object result;

    //成功信息
    String success;

    //创建实例
    public static ResponseResult getResponseResult(){
        return new ResponseResult();
    }

    //登录成功标识
    String token;

    //表示token的唯一字符串
    String tokenkey;

    //需要回显的菜单id
    Long[] menuids;
}
