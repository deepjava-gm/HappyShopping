package com.changgou.order.service;

import java.util.Map;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-10 16:50
 */

public interface CartService {
    //添加购物车
    void addCart(String skuId,Integer num,String username);

    //查询购物车数据
    Map list(String username);
}
