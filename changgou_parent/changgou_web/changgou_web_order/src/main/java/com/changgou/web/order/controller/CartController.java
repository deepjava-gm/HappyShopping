package com.changgou.web.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


  /*//添加2  输入框添加
    @GetMapping("/add2")
    @ResponseBody
    public Result<Map> add2(String id,Integer num){

        Map map1 = cartFeign.list();

        List list = (List) map1.get("orderItemList");

        Iterator iterator = list.iterator();
        while (iterator.hasNext()){

//            该obj 对象是一个LinkedHashMap
            Object obj = iterator.next();

//            将obj 对象（LinkedHashMap）  转为javabean （orderItem）
            OrderItem orderItem = new ObjectMapper().convertValue(obj, OrderItem.class);

            if(orderItem.getSkuId().equals(id)){
                Integer num1 = orderItem.getNum();
                int i = num - num1;
                cartFeign.addCart(id,i);
            }
        }

        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }*/


    //添加2  输入框添加
    @GetMapping("/add2")
    @ResponseBody
    public Result<Map> add2(String id,Integer num){

        Map map1 = cartFeign.list();

        List<LinkedHashMap> list = (List) map1.get("orderItemList");

        for (LinkedHashMap linkedHashMap : list) {
            if(linkedHashMap.get("skuId").equals(id)){
                Integer num1 = (Integer) linkedHashMap.get("num");
                int i = num - num1;
                cartFeign.addCart(id,i);
            }
        }

        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }



}
