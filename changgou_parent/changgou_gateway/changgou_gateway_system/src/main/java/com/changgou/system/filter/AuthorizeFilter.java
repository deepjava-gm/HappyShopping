package com.changgou.system.filter;

import com.changgou.util.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-05-30 15:39
 * JWT校验
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        1、获取请求对象
        ServerHttpRequest request = exchange.getRequest();

//        2、获取响应对象
        ServerHttpResponse response = exchange.getResponse();

//        3、判断当前请求是否为登录请求  是直接放行

        if (request.getURI().getPath().contains("/admin/login")) {

//            放行
            return chain.filter(exchange);
        }

//        4、获取当前所有请求头信息
        HttpHeaders headers = request.getHeaders();

//        5、获取jwt令牌信息
        String jwtToken = headers.getFirst("token");

//        6、判断当前令牌是否存在
        if (StringUtils.isEmpty(jwtToken)) {
//            如果不存在则向客户端返回错误信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED); //表示当前用户认证失败
            return response.setComplete();
        }

//        6.1、如果令牌存在，解析jwt令牌，判断该令牌是否合法，如果令牌不合法则向客户端返回错误提示信息

        try {
//          解析令牌
            JwtUtil.parseJWT(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
//            令牌解析失败  向客户端返回错误信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }


//        6.2、如果令牌合法则放行

//        同时可以重置token的有效时间

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
