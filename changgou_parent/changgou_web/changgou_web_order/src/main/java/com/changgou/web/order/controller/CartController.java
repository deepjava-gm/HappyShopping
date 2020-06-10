package com.changgou.web.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 作者: kinggm Email:731586355@qq.com
 * 时间:  2020-06-10 19:24
 */

@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    //查询
    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.list();
        model.addAttribute("items",map);
        return "cart";
    }


    //添加
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(String id,Integer num){
        cartFeign.addCart(id,num);
        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }

    //添加2  输入框添加   有Bug问题待解决   com.alibaba.fastjson.JSONObject cannot be cast to com.changgou.order.pojo.OrderItem
    @GetMapping("/add2")
    @ResponseBody
    public Result<Map> add2(String id,Integer num){

        Map map1 = cartFeign.list();

        Object orderItemList = map1.get("orderItemList");
        String string = JSON.toJSONString(orderItemList);
        List<OrderItem> list = JSON.parseObject(string, List.class);

        Iterator<OrderItem> iterator = list.iterator();
        while (iterator.hasNext()){
            OrderItem orderItem = iterator.next();
            if(orderItem.getSkuId()==id){
                Integer num1 = orderItem.getNum();
                int i = num1 - num;
                cartFeign.addCart(id,i);
            }
        }


        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }


}
