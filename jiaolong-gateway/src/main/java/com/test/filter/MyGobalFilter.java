package com.test.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.test.jwt.JWTUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：WSY
 * @description :全局过滤器
 * @date ：Created in 2019/8/6 9:49
 */
@Component
public class MyGobalFilter implements GlobalFilter {

    @Value("${my.auth.urls}")
    String[] urls;

    @Value("${my.auth.loginPath}")
    String loginpath;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("---myfliter---");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String urlPath = request.getURI().toString();
        List<String> strings = Arrays.asList(urls);
        //是否包含不需要过滤的路径
        if (strings.contains(urlPath)) {
            return chain.filter(exchange);
        } else {
            List<String> token = request.getHeaders().get("token");
            JSONObject jsonObject = null;
            try {
                jsonObject = JWTUtils.decodeJwtTocken(token.get(0));
                String generateToken = JWTUtils.generateToken(jsonObject.toJSONString());
                response.getHeaders().set("token", generateToken);
            } catch (JwtException e) {
                e.printStackTrace();
                response.getHeaders().set("loginpath", loginpath);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                //跳转登录页面
                return exchange.getResponse().setComplete();
            }
            //获取id
            String userid = jsonObject.get("id").toString();
            //判断权限
            Boolean key = redisTemplate.opsForHash().hasKey("userauth" + userid, urlPath);
            if (key) {
                return chain.filter(exchange);
            } else {
                throw new RuntimeException("不能访问");
            }
        }
    }
}
